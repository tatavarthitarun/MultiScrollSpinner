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
import androidx.core.content.ContextCompat

class MultiScrollSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

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

        // Make the entire view clickable
        isClickable = true
        isFocusable = true
        setOnClickListener {
            toggleDropdown()
        }
        
        // Also set click listener on the child view to ensure clicks are captured
        view.setOnClickListener {
            toggleDropdown()
        }
    }

    fun setItems(items: List<String>) {
        this.items = items
        if (items.isNotEmpty() && selectedPosition == -1) {
            // Don't auto-select, just show hint
            updateSelectedText()
        } else if (selectedPosition >= 0 && selectedPosition < items.size) {
            updateSelectedText()
        } else {
            updateSelectedText()
        }
    }

    fun setSelection(position: Int) {
        if (position >= 0 && position < items.size) {
            selectedPosition = position
            updateSelectedText()
            onItemSelectedListener?.invoke(position, items[position])
        }
    }

    fun getSelectedPosition(): Int = selectedPosition

    fun getSelectedItem(): String? {
        return if (selectedPosition >= 0 && selectedPosition < items.size) {
            items[selectedPosition]
        } else {
            null
        }
    }

    fun setOnItemSelectedListener(listener: (Int, String) -> Unit) {
        this.onItemSelectedListener = listener
    }

    private fun updateSelectedText() {
        if (selectedPosition >= 0 && selectedPosition < items.size) {
            selectedText?.text = items[selectedPosition]
            // Reset scroll position when text changes
            selectedTextScrollView?.post {
                selectedTextScrollView?.scrollTo(0, 0)
            }
        }
    }

    private fun toggleDropdown() {
        android.util.Log.d("MultiScrollSpinner", "toggleDropdown called, items.size=${items.size}")
        if (items.isEmpty()) {
            android.util.Log.w("MultiScrollSpinner", "Items list is empty!")
            return
        }

        if (popup?.isShowing() == true) {
            android.util.Log.d("MultiScrollSpinner", "Dismissing dropdown")
            dismissDropdown()
        } else {
            android.util.Log.d("MultiScrollSpinner", "Showing dropdown")
            showDropdown()
        }
    }

    private fun showDropdown() {
        if (items.isEmpty()) {
            return
        }

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
            val fromDegrees = if (expanded) 0f else 180f
            val toDegrees = if (expanded) 180f else 0f
            
            val rotateAnimation = RotateAnimation(
                fromDegrees,
                toDegrees,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f,
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f
            ).apply {
                duration = 200
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
