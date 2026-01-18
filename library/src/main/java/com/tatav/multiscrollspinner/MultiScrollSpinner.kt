package com.tatav.multiscrollspinner

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.tatav.multiscrollspinner.library.R as LibraryR

/**
 * A custom spinner widget that supports both horizontal scrolling for long item text
 * and vertical scrolling for multiple items.
 *
 * Features:
 * - Horizontal scrolling within each dropdown item for long text
 * - Vertical scrolling for lists with many items
 * - Material Design styling
 * - Extensive customization options via XML attributes or programmatically
 *
 * XML Attributes:
 * - ms_dropdownArrow: Drawable resource for dropdown arrow
 * - ms_arrowTint: Color for arrow tinting
 * - ms_arrowWidth/Height: Arrow dimensions
 * - ms_textSize: Text size for selected item
 * - ms_textColor: Text color for selected item
 * - ms_hintText: Hint text when no item selected
 * - ms_hintTextColor: Color for hint text
 * - ms_itemTextSize: Text size for dropdown items
 * - ms_itemTextColor: Text color for dropdown items
 * - ms_selectedItemBackground: Background color for selected item
 * - ms_selectedItemTextColor: Text color for selected item
 * - ms_itemHeight: Height of each dropdown item
 * - ms_maxDropdownHeight: Maximum height of dropdown
 * - ms_popupBackground: Background color/drawable for popup
 * - ms_showToast: Boolean to enable/disable automatic toast on item selection
 */
class MultiScrollSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val ARROW_ANIMATION_DURATION_MS = 200L
        private const val ARROW_EXPANDED_DEGREES = 180f
        private const val ARROW_COLLAPSED_DEGREES = 0f
    }

    // Customization properties
    private var dropdownArrowDrawable: Drawable? = null
    private var arrowTint: Int? = null
    private var arrowWidth: Int = 24
    private var arrowHeight: Int = 24
    private var textSize: Float? = null
    private var textColor: Int? = null
    private var hintText: String? = null
    private var hintTextColor: Int? = null
    private var itemTextSize: Float? = null
    private var itemTextColor: Int? = null
    private var selectedItemBackground: Int? = null
    private var selectedItemTextColor: Int? = null
    private var itemHeight: Int? = null
    private var maxDropdownHeight: Int? = null
    private var popupBackground: Any? = null
    private var showToast: Boolean = false

    private var items: List<String> = emptyList()
    private var selectedPosition: Int = -1
    private var selectedText: TextView? = null
    private var selectedTextScrollView: HorizontalScrollView? = null
    private var dropdownArrow: ImageView? = null
    private var popup: MultiScrollSpinnerPopup? = null
    private var onItemSelectedListener: ((Int, String) -> Unit)? = null

    init {
        initAttributes(attrs, defStyleAttr)
        initView()
    }

    private fun initAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs,
            LibraryR.styleable.MultiScrollSpinner,
            defStyleAttr,
            0
        )

        try {
            // Arrow customization
            dropdownArrowDrawable = typedArray.getDrawable(LibraryR.styleable.MultiScrollSpinner_ms_dropdownArrow)
            arrowTint = if (typedArray.hasValue(LibraryR.styleable.MultiScrollSpinner_ms_arrowTint)) {
                typedArray.getColor(LibraryR.styleable.MultiScrollSpinner_ms_arrowTint, 0)
            } else null
            arrowWidth = typedArray.getDimensionPixelSize(
                LibraryR.styleable.MultiScrollSpinner_ms_arrowWidth,
                resources.getDimensionPixelSize(android.R.dimen.app_icon_size) / 2
            )
            arrowHeight = typedArray.getDimensionPixelSize(
                LibraryR.styleable.MultiScrollSpinner_ms_arrowHeight,
                resources.getDimensionPixelSize(android.R.dimen.app_icon_size) / 2
            )

            // Text customization
            textSize = if (typedArray.hasValue(LibraryR.styleable.MultiScrollSpinner_ms_textSize)) {
                typedArray.getDimensionPixelSize(
                    LibraryR.styleable.MultiScrollSpinner_ms_textSize,
                    0
                ).toFloat()
            } else null
            textColor = if (typedArray.hasValue(LibraryR.styleable.MultiScrollSpinner_ms_textColor)) {
                typedArray.getColor(LibraryR.styleable.MultiScrollSpinner_ms_textColor, 0)
            } else null
            hintText = typedArray.getString(LibraryR.styleable.MultiScrollSpinner_ms_hintText)
            hintTextColor = if (typedArray.hasValue(LibraryR.styleable.MultiScrollSpinner_ms_hintTextColor)) {
                typedArray.getColor(LibraryR.styleable.MultiScrollSpinner_ms_hintTextColor, 0)
            } else null

            // Item customization
            itemTextSize = if (typedArray.hasValue(LibraryR.styleable.MultiScrollSpinner_ms_itemTextSize)) {
                typedArray.getDimensionPixelSize(
                    LibraryR.styleable.MultiScrollSpinner_ms_itemTextSize,
                    0
                ).toFloat()
            } else null
            itemTextColor = if (typedArray.hasValue(LibraryR.styleable.MultiScrollSpinner_ms_itemTextColor)) {
                typedArray.getColor(LibraryR.styleable.MultiScrollSpinner_ms_itemTextColor, 0)
            } else null
            selectedItemBackground = if (typedArray.hasValue(LibraryR.styleable.MultiScrollSpinner_ms_selectedItemBackground)) {
                typedArray.getColor(LibraryR.styleable.MultiScrollSpinner_ms_selectedItemBackground, 0)
            } else null
            selectedItemTextColor = if (typedArray.hasValue(LibraryR.styleable.MultiScrollSpinner_ms_selectedItemTextColor)) {
                typedArray.getColor(LibraryR.styleable.MultiScrollSpinner_ms_selectedItemTextColor, 0)
            } else null
            itemHeight = if (typedArray.hasValue(LibraryR.styleable.MultiScrollSpinner_ms_itemHeight)) {
                typedArray.getDimensionPixelSize(LibraryR.styleable.MultiScrollSpinner_ms_itemHeight, 0)
            } else null

            // Popup customization
            maxDropdownHeight = if (typedArray.hasValue(LibraryR.styleable.MultiScrollSpinner_ms_maxDropdownHeight)) {
                typedArray.getDimensionPixelSize(LibraryR.styleable.MultiScrollSpinner_ms_maxDropdownHeight, 0)
            } else null
            popupBackground = typedArray.getDrawable(LibraryR.styleable.MultiScrollSpinner_ms_popupBackground)
                ?: if (typedArray.hasValue(LibraryR.styleable.MultiScrollSpinner_ms_popupBackground)) {
                    typedArray.getColor(LibraryR.styleable.MultiScrollSpinner_ms_popupBackground, 0)
                } else null
            
            // Toast customization
            showToast = typedArray.getBoolean(LibraryR.styleable.MultiScrollSpinner_ms_showToast, false)
        } finally {
            typedArray.recycle()
        }
    }

    private fun initView() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(LibraryR.layout.layout_multiscroll_spinner, this, true)
        
        selectedText = view.findViewById(LibraryR.id.selectedText)
        selectedTextScrollView = view.findViewById(LibraryR.id.selectedTextScrollView)
        dropdownArrow = view.findViewById(LibraryR.id.dropdownArrow)

        // Apply arrow customization
        applyArrowCustomization()

        // Apply text customization
        applyTextCustomization()

        isClickable = true
        isFocusable = true
        setOnClickListener { toggleDropdown() }
        view.setOnClickListener { toggleDropdown() }
    }

    private fun applyArrowCustomization() {
        dropdownArrow?.let { arrow ->
            dropdownArrowDrawable?.let { drawable ->
                arrow.setImageDrawable(drawable)
            }
            arrowTint?.let { tint ->
                arrow.setColorFilter(tint)
            }
            arrow.layoutParams?.let { params ->
                params.width = arrowWidth
                params.height = arrowHeight
                arrow.layoutParams = params
            }
        }
    }

    private fun applyTextCustomization() {
        selectedText?.let { textView ->
            textSize?.let { size ->
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
            }
            textColor?.let { color ->
                textView.setTextColor(color)
            }
            hintText?.let { hint ->
                if (selectedPosition == -1) {
                    textView.text = hint
                }
            }
            hintTextColor?.let { color ->
                if (selectedPosition == -1) {
                    textView.setTextColor(color)
                }
            }
        }
    }

    /**
     * Sets the list of items to display in the spinner.
     *
     * @param items The list of string items to display
     */
    fun setItems(items: List<String>) {
        this.items = items
        updateSelectedText()
    }

    /**
     * Programmatically selects an item at the given position.
     *
     * @param position The position of the item to select (0-based index)
     */
    fun setSelection(position: Int) {
        if (position >= 0 && position < items.size) {
            selectedPosition = position
            updateSelectedText()
            
            // Show toast if enabled
            if (showToast) {
                Toast.makeText(
                    context,
                    "Selected: ${items[position]} (Position: $position)",
                    Toast.LENGTH_SHORT
                ).show()
            }
            
            onItemSelectedListener?.invoke(position, items[position])
        }
    }

    /**
     * Gets the currently selected item position.
     *
     * @return The selected position, or -1 if no item is selected
     */
    fun getSelectedPosition(): Int = selectedPosition

    /**
     * Gets the currently selected item.
     *
     * @return The selected item string, or null if no item is selected
     */
    fun getSelectedItem(): String? {
        return if (selectedPosition >= 0 && selectedPosition < items.size) {
            items[selectedPosition]
        } else {
            null
        }
    }

    /**
     * Sets a listener to be invoked when an item is selected.
     *
     * @param listener A callback that receives the position and item string
     */
    fun setOnItemSelectedListener(listener: (Int, String) -> Unit) {
        this.onItemSelectedListener = listener
    }

    /**
     * Sets whether to show a toast message when an item is selected.
     *
     * @param show true to show toast, false to hide it (default: false)
     */
    fun setShowToast(show: Boolean) {
        this.showToast = show
    }

    /**
     * Gets whether toast is enabled for item selection.
     *
     * @return true if toast is enabled, false otherwise
     */
    fun isShowToastEnabled(): Boolean = showToast

    // Customization setters

    /**
     * Sets the dropdown arrow drawable resource.
     *
     * @param drawableRes The drawable resource ID
     */
    fun setDropdownArrow(drawableRes: Int) {
        dropdownArrowDrawable = ContextCompat.getDrawable(context, drawableRes)
        applyArrowCustomization()
    }

    /**
     * Sets the dropdown arrow drawable.
     *
     * @param drawable The drawable to use
     */
    fun setDropdownArrow(drawable: Drawable?) {
        dropdownArrowDrawable = drawable
        applyArrowCustomization()
    }

    /**
     * Sets the arrow tint color.
     *
     * @param color The color resource ID or color int
     */
    fun setArrowTint(color: Int) {
        arrowTint = color
        applyArrowCustomization()
    }

    /**
     * Sets the text size for the selected item.
     *
     * @param size Text size in sp
     */
    fun setTextSize(size: Float) {
        textSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            size,
            resources.displayMetrics
        )
        applyTextCustomization()
    }

    /**
     * Sets the text color for the selected item.
     *
     * @param color The color resource ID or color int
     */
    fun setTextColor(color: Int) {
        textColor = color
        applyTextCustomization()
    }

    /**
     * Sets the hint text when no item is selected.
     *
     * @param hint The hint text string
     */
    fun setHintText(hint: String) {
        hintText = hint
        updateSelectedText()
    }

    /**
     * Sets the hint text color.
     *
     * @param color The color resource ID or color int
     */
    fun setHintTextColor(color: Int) {
        hintTextColor = color
        updateSelectedText()
    }

    /**
     * Sets the text size for dropdown items.
     *
     * @param size Text size in sp
     */
    fun setItemTextSize(size: Float) {
        itemTextSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            size,
            resources.displayMetrics
        )
    }

    /**
     * Sets the text color for dropdown items.
     *
     * @param color The color resource ID or color int
     */
    fun setItemTextColor(color: Int) {
        itemTextColor = color
    }

    /**
     * Sets the background color for selected items.
     *
     * @param color The color resource ID or color int
     */
    fun setSelectedItemBackground(color: Int) {
        selectedItemBackground = color
    }

    /**
     * Sets the text color for selected items.
     *
     * @param color The color resource ID or color int
     */
    fun setSelectedItemTextColor(color: Int) {
        selectedItemTextColor = color
    }

    /**
     * Sets the height for dropdown items.
     *
     * @param height Height in dp
     */
    fun setItemHeight(height: Int) {
        itemHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            height.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    /**
     * Sets the maximum height for the dropdown.
     *
     * @param height Height in dp
     */
    fun setMaxDropdownHeight(height: Int) {
        maxDropdownHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            height.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    /**
     * Sets the popup background.
     *
     * @param background Drawable resource ID or color int
     */
    fun setPopupBackground(background: Int) {
        popupBackground = background
    }

    private fun updateSelectedText() {
        selectedText?.let { textView ->
            if (selectedPosition >= 0 && selectedPosition < items.size) {
                textView.text = items[selectedPosition]
                textColor?.let { textView.setTextColor(it) }
            } else {
                textView.text = hintText ?: "Select an item"
                hintTextColor?.let { textView.setTextColor(it) }
            }
            selectedTextScrollView?.post {
                selectedTextScrollView?.scrollTo(0, 0)
            }
        }
    }

    private fun toggleDropdown() {
        if (items.isEmpty()) return

        if (popup?.isShowing() == true) {
            dismissDropdown()
        } else {
            showDropdown()
        }
    }

    private fun showDropdown() {
        if (items.isEmpty()) return

        popup = MultiScrollSpinnerPopup(
            context,
            items,
            selectedPosition,
            onItemSelected = { position, item ->
                setSelection(position)
                onItemSelectedListener?.invoke(position, item)
            },
            onDismiss = {
                animateArrow(false)
            },
            customization = getCustomizationConfig()
        )

        popup?.show(this)
        animateArrow(true)
    }

    private fun getCustomizationConfig(): MultiScrollSpinnerCustomization {
        return MultiScrollSpinnerCustomization(
            itemTextSize = itemTextSize,
            itemTextColor = itemTextColor,
            selectedItemBackground = selectedItemBackground,
            selectedItemTextColor = selectedItemTextColor,
            itemHeight = itemHeight,
            maxDropdownHeight = maxDropdownHeight,
            popupBackground = popupBackground
        )
    }

    private fun dismissDropdown() {
        popup?.dismiss()
        popup = null
        animateArrow(false)
    }

    private fun animateArrow(expanded: Boolean) {
        dropdownArrow?.let { arrow ->
            val rotateAnimation = RotateAnimation(
                if (expanded) ARROW_COLLAPSED_DEGREES else ARROW_EXPANDED_DEGREES,
                if (expanded) ARROW_EXPANDED_DEGREES else ARROW_COLLAPSED_DEGREES,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f
            ).apply {
                duration = ARROW_ANIMATION_DURATION_MS
                fillAfter = true
            }
            
            arrow.startAnimation(rotateAnimation)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dismissDropdown()
    }
}

/**
 * Data class to hold customization configuration for the spinner.
 */
data class MultiScrollSpinnerCustomization(
    val itemTextSize: Float? = null,
    val itemTextColor: Int? = null,
    val selectedItemBackground: Int? = null,
    val selectedItemTextColor: Int? = null,
    val itemHeight: Int? = null,
    val maxDropdownHeight: Int? = null,
    val popupBackground: Any? = null
)
