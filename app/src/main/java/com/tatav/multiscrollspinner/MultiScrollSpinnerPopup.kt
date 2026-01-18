package com.tatav.multiscrollspinner

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MultiScrollSpinnerPopup(
    private val context: Context,
    private val items: List<String>,
    private val selectedPosition: Int,
    private val onItemSelected: (Int, String) -> Unit,
    private val onDismiss: () -> Unit
) {
    private var popupWindow: PopupWindow? = null
    private var adapter: MultiScrollSpinnerAdapter? = null

    fun show(anchorView: View) {
        // Ensure anchor view is measured
        if (anchorView.width == 0 || anchorView.height == 0) {
            anchorView.measure(
                View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            )
        }
        
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_multiscroll_spinner, null)
        
        val recyclerView: RecyclerView = popupView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        
        // Ensure RecyclerView doesn't block item clicks
        recyclerView.isNestedScrollingEnabled = true
        
        adapter = MultiScrollSpinnerAdapter(items, selectedPosition) { position, item ->
            android.util.Log.d("MultiScrollSpinnerPopup", "Item selected callback: position=$position, item=$item")
            onItemSelected(position, item)
            dismiss()
        }
        recyclerView.adapter = adapter

        // Calculate max height (60% of screen height or 400dp, whichever is smaller)
        val displayMetrics = context.resources.displayMetrics
        val maxHeight = (displayMetrics.heightPixels * 0.6).toInt().coerceAtMost(
            (400 * displayMetrics.density).toInt()
        )
        
        // Calculate approximate height based on item count
        // Each item is approximately 48dp (minHeight) + padding
        val itemHeight = (48 * displayMetrics.density).toInt()
        val padding = (16 * displayMetrics.density).toInt() // top + bottom padding
        val calculatedHeight = (items.size * itemHeight + padding).coerceAtMost(maxHeight).coerceAtLeast(itemHeight)
        
        // Set a fixed height for RecyclerView to ensure it renders
        val layoutParams = recyclerView.layoutParams ?: ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            calculatedHeight
        )
        layoutParams.height = calculatedHeight
        recyclerView.layoutParams = layoutParams

        // Get anchor width, use screen width as fallback
        val popupWidth = if (anchorView.width > 0) {
            anchorView.width
        } else {
            displayMetrics.widthPixels
        }

        // Create popup window
        popupWindow = PopupWindow(
            popupView,
            popupWidth,
            calculatedHeight,
            true
        ).apply {
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(context, android.R.color.white)))
            elevation = 8f
            isOutsideTouchable = true
            isFocusable = true
            
            setOnDismissListener {
                onDismiss()
            }
        }

        // Show popup below anchor view
        android.util.Log.d("MultiScrollSpinnerPopup", "Attempting to show popup, isAttached=${anchorView.isAttachedToWindow}, width=$popupWidth, height=$calculatedHeight, items=${items.size}")
        
        if (anchorView.isAttachedToWindow) {
            try {
                popupWindow?.showAsDropDown(anchorView, 0, 0)
                android.util.Log.d("MultiScrollSpinnerPopup", "Popup shown successfully, isShowing=${popupWindow?.isShowing}")
            } catch (e: Exception) {
                android.util.Log.e("MultiScrollSpinnerPopup", "Error showing popup", e)
                // Try with post as fallback
                anchorView.post {
                    try {
                        popupWindow?.showAsDropDown(anchorView, 0, 0)
                        android.util.Log.d("MultiScrollSpinnerPopup", "Popup shown via post, isShowing=${popupWindow?.isShowing}")
                    } catch (e2: Exception) {
                        android.util.Log.e("MultiScrollSpinnerPopup", "Error showing popup via post", e2)
                    }
                }
            }
        } else {
            android.util.Log.w("MultiScrollSpinnerPopup", "Anchor view not attached, using post")
            anchorView.post {
                if (anchorView.isAttachedToWindow) {
                    try {
                        popupWindow?.showAsDropDown(anchorView, 0, 0)
                        android.util.Log.d("MultiScrollSpinnerPopup", "Popup shown via post, isShowing=${popupWindow?.isShowing}")
                    } catch (e: Exception) {
                        android.util.Log.e("MultiScrollSpinnerPopup", "Error showing popup via post", e)
                    }
                }
            }
        }
    }

    fun dismiss() {
        popupWindow?.dismiss()
        popupWindow = null
    }

    fun isShowing(): Boolean = popupWindow?.isShowing == true
}
