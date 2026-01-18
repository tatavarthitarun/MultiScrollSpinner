package com.tatav.multiscrollspinner

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView

/**
 * A custom spinner widget that supports both horizontal scrolling for long item text
 * and vertical scrolling for multiple items.
 *
 * Features:
 * - Horizontal scrolling within each dropdown item for long text
 * - Vertical scrolling for lists with many items
 * - Material Design styling
 * - Easy to integrate and customize
 *
 * @property items The list of items to display in the spinner
 * @property selectedPosition The currently selected item position (-1 if none)
 * @property onItemSelectedListener Callback invoked when an item is selected
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

    private var items: List<String> = emptyList()
    private var selectedPosition: Int = -1
    private var selectedText: TextView? = null
    private var selectedTextScrollView: HorizontalScrollView? = null
    private var dropdownArrow: ImageView? = null
    private var popup: MultiScrollSpinnerPopup? = null
    private var onItemSelectedListener: ((Int, String) -> Unit)? = null

    init {
        initView()
    }

    private fun initView() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_multiscroll_spinner, this, true)
        
        selectedText = view.findViewById(R.id.selectedText)
        selectedTextScrollView = view.findViewById(R.id.selectedTextScrollView)
        dropdownArrow = view.findViewById(R.id.dropdownArrow)

        isClickable = true
        isFocusable = true
        setOnClickListener { toggleDropdown() }
        view.setOnClickListener { toggleDropdown() }
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

    private fun updateSelectedText() {
        if (selectedPosition >= 0 && selectedPosition < items.size) {
            selectedText?.text = items[selectedPosition]
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
            }
        )

        popup?.show(this)
        animateArrow(true)
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
