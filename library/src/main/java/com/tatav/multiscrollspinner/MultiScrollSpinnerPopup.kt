package com.tatav.multiscrollspinner

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tatav.multiscrollspinner.library.R as LibraryR

/**
 * Manages the popup window that displays the spinner dropdown list.
 *
 * Handles popup creation, positioning, sizing, and dismissal.
 */
class MultiScrollSpinnerPopup(
    private val context: Context,
    private val items: List<String>,
    private val selectedPosition: Int,
    private val onItemSelected: (Int, String) -> Unit,
    private val onDismiss: () -> Unit,
    private val customization: MultiScrollSpinnerCustomization? = null
) {
    companion object {
        private const val MAX_HEIGHT_SCREEN_PERCENT = 0.6f
        private const val MAX_HEIGHT_DP = 400
        private const val ITEM_HEIGHT_DP = 48
        private const val POPUP_PADDING_DP = 16
        private const val POPUP_ELEVATION = 8f
    }

    private var popupWindow: PopupWindow? = null
    private var adapter: MultiScrollSpinnerAdapter? = null

    /**
     * Shows the popup dropdown below the anchor view.
     *
     * @param anchorView The view to anchor the popup to
     */
    fun show(anchorView: View) {
        ensureAnchorMeasured(anchorView)
        
        val popupView = LayoutInflater.from(context)
            .inflate(LibraryR.layout.popup_multiscroll_spinner, null)
        
        val recyclerView: RecyclerView = popupView.findViewById(LibraryR.id.recyclerView)
        setupRecyclerView(recyclerView)
        
        val displayMetrics = context.resources.displayMetrics
        val popupWidth = getPopupWidth(anchorView, displayMetrics)
        val popupHeight = calculatePopupHeight(items.size, displayMetrics)
        
        setupRecyclerViewHeight(recyclerView, popupHeight)
        createAndShowPopup(popupView, anchorView, popupWidth, popupHeight)
    }

    /**
     * Dismisses the popup if it's currently showing.
     */
    fun dismiss() {
        popupWindow?.dismiss()
        popupWindow = null
        adapter = null
    }

    /**
     * Checks if the popup is currently showing.
     *
     * @return true if the popup is showing, false otherwise
     */
    fun isShowing(): Boolean = popupWindow?.isShowing == true

    private fun ensureAnchorMeasured(anchorView: View) {
        if (anchorView.width == 0 || anchorView.height == 0) {
            anchorView.measure(
                View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            )
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.isNestedScrollingEnabled = true
        
        adapter = MultiScrollSpinnerAdapter(items, selectedPosition, { position, item ->
            onItemSelected(position, item)
            dismiss()
        }, customization)
        recyclerView.adapter = adapter
    }

    private fun calculatePopupHeight(itemCount: Int, displayMetrics: android.util.DisplayMetrics): Int {
        val maxHeight = customization?.maxDropdownHeight
            ?: (displayMetrics.heightPixels * MAX_HEIGHT_SCREEN_PERCENT).toInt().coerceAtMost(
                (MAX_HEIGHT_DP * displayMetrics.density).toInt()
            )
        
        val itemHeight = customization?.itemHeight
            ?: (ITEM_HEIGHT_DP * displayMetrics.density).toInt()
        val padding = (POPUP_PADDING_DP * displayMetrics.density).toInt()
        val calculatedHeight = (itemCount * itemHeight + padding).coerceAtMost(maxHeight)
        
        return calculatedHeight.coerceAtLeast(itemHeight)
    }

    private fun getPopupWidth(anchorView: View, displayMetrics: android.util.DisplayMetrics): Int {
        return if (anchorView.width > 0) {
            anchorView.width
        } else {
            displayMetrics.widthPixels
        }
    }

    private fun setupRecyclerViewHeight(recyclerView: RecyclerView, height: Int) {
        val layoutParams = recyclerView.layoutParams ?: ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            height
        )
        layoutParams.height = height
        recyclerView.layoutParams = layoutParams
    }

    private fun createAndShowPopup(
        popupView: View,
        anchorView: View,
        width: Int,
        height: Int
    ) {
        val backgroundDrawable = getPopupBackground()
        
        popupWindow = PopupWindow(
            popupView,
            width,
            height,
            true
        ).apply {
            setBackgroundDrawable(backgroundDrawable)
            elevation = POPUP_ELEVATION
            isOutsideTouchable = true
            isFocusable = true
            setOnDismissListener { onDismiss() }
        }

        showPopupSafely(anchorView)
    }

    private fun getPopupBackground(): Drawable {
        return when (val bg = customization?.popupBackground) {
            is Drawable -> bg
            is Int -> ColorDrawable(bg)
            else -> ColorDrawable(ContextCompat.getColor(context, android.R.color.white))
        }
    }

    private fun showPopupSafely(anchorView: View) {
        if (anchorView.isAttachedToWindow) {
            try {
                popupWindow?.showAsDropDown(anchorView, 0, 0)
            } catch (e: Exception) {
                // Fallback: try showing after view is laid out
                anchorView.post {
                    try {
                        popupWindow?.showAsDropDown(anchorView, 0, 0)
                    } catch (e2: Exception) {
                        // Silently fail if popup cannot be shown
                    }
                }
            }
        } else {
            anchorView.post {
                if (anchorView.isAttachedToWindow) {
                    try {
                        popupWindow?.showAsDropDown(anchorView, 0, 0)
                    } catch (e: Exception) {
                        // Silently fail if popup cannot be shown
                    }
                }
            }
        }
    }
}
