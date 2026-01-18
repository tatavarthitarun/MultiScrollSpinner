package com.tatav.multiscrollspinner

import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView adapter for displaying spinner items with horizontal scrolling capability.
 *
 * Each item can scroll horizontally if the text is too long, and items are selectable
 * with visual feedback for the selected state.
 */
class MultiScrollSpinnerAdapter(
    private val items: List<String>,
    private var selectedPosition: Int = -1,
    private val onItemSelected: (Int, String) -> Unit
) : RecyclerView.Adapter<MultiScrollSpinnerAdapter.ItemViewHolder>() {

    companion object {
        private const val INVALID_POSITION = -1
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.itemText)
        val horizontalScrollView: HorizontalScrollView = itemView.findViewById(R.id.horizontalScrollView)

        init {
            itemView.isClickable = true
            itemView.isFocusable = true
            horizontalScrollView.isClickable = false
            horizontalScrollView.isFocusable = false
            horizontalScrollView.isFocusableInTouchMode = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_multiscroll_spinner, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.textView.text = items[position]
        
        // Reset horizontal scroll position when binding
        holder.horizontalScrollView.post {
            holder.horizontalScrollView.scrollTo(0, 0)
        }
        
        // Set click listener
        val clickListener = View.OnClickListener {
            val clickedPosition = holder.adapterPosition
            if (clickedPosition != RecyclerView.NO_POSITION && clickedPosition < items.size) {
                updateSelection(clickedPosition)
                onItemSelected(clickedPosition, items[clickedPosition])
            }
        }
        
        holder.itemView.setOnClickListener(clickListener)
        holder.textView.setOnClickListener(clickListener)
        
        // Update visual state
        updateItemAppearance(holder, position)
    }

    override fun getItemCount(): Int = items.size

    /**
     * Updates the selected position and notifies the adapter of changes.
     *
     * @param position The new selected position
     */
    fun setSelectedPosition(position: Int) {
        if (position != selectedPosition) {
            val previousSelected = selectedPosition
            selectedPosition = position
            if (previousSelected != INVALID_POSITION) {
                notifyItemChanged(previousSelected)
            }
            if (selectedPosition != INVALID_POSITION) {
                notifyItemChanged(selectedPosition)
            }
        }
    }

    /**
     * Gets the currently selected position.
     *
     * @return The selected position, or -1 if none
     */
    fun getSelectedPosition(): Int = selectedPosition

    private fun updateSelection(newPosition: Int) {
        val previousSelected = selectedPosition
        selectedPosition = newPosition
        
        if (previousSelected != INVALID_POSITION) {
            notifyItemChanged(previousSelected)
        }
        notifyItemChanged(selectedPosition)
    }

    private fun updateItemAppearance(holder: ItemViewHolder, position: Int) {
        val context = holder.itemView.context
        
        if (position == selectedPosition) {
            // Selected state
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(context, android.R.color.holo_blue_light)
            )
            holder.textView.setTextColor(
                ContextCompat.getColor(context, android.R.color.white)
            )
        } else {
            // Normal state
            holder.itemView.background = ContextCompat.getDrawable(
                context,
                android.R.drawable.list_selector_background
            )
            holder.textView.setTextColor(getTextColorPrimary(context))
        }
    }

    private fun getTextColorPrimary(context: android.content.Context): Int {
        val typedArray: TypedArray = context.obtainStyledAttributes(
            intArrayOf(android.R.attr.textColorPrimary)
        )
        val textColor = typedArray.getColor(0, 0)
        typedArray.recycle()
        return textColor
    }
}
