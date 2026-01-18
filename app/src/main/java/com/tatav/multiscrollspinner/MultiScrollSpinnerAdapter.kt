package com.tatav.multiscrollspinner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class MultiScrollSpinnerAdapter(
    private val items: List<String>,
    private var selectedPosition: Int = -1,
    private val onItemSelected: (Int, String) -> Unit
) : RecyclerView.Adapter<MultiScrollSpinnerAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.itemText)
        val horizontalScrollView: HorizontalScrollView = itemView.findViewById(R.id.horizontalScrollView)

        init {
            // Make sure the item view is clickable
            itemView.isClickable = true
            itemView.isFocusable = true
            
            // Make HorizontalScrollView not intercept clicks but allow scrolling
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
        
        // Reset scroll position when binding
        holder.horizontalScrollView.post {
            holder.horizontalScrollView.scrollTo(0, 0)
        }
        
        // Re-set click listener in onBindViewHolder to ensure it's always current
        val clickListener = View.OnClickListener { view ->
            val clickedPosition = holder.adapterPosition
            android.util.Log.d("MultiScrollSpinnerAdapter", "Item clicked in onBindViewHolder at position: $clickedPosition, view=$view")
            if (clickedPosition != RecyclerView.NO_POSITION && clickedPosition < items.size) {
                val previousSelected = selectedPosition
                selectedPosition = clickedPosition
                
                if (previousSelected != -1) {
                    notifyItemChanged(previousSelected)
                }
                notifyItemChanged(selectedPosition)
                
                android.util.Log.d("MultiScrollSpinnerAdapter", "Calling onItemSelected with position: $clickedPosition, item: ${items[clickedPosition]}")
                onItemSelected(clickedPosition, items[clickedPosition])
            } else {
                android.util.Log.w("MultiScrollSpinnerAdapter", "Invalid position: $clickedPosition, items.size=${items.size}")
            }
        }
        
        holder.itemView.setOnClickListener(clickListener)
        holder.textView.setOnClickListener(clickListener)
        
        // Also ensure the entire view hierarchy is clickable
        holder.itemView.isClickable = true
        holder.itemView.isFocusable = true
        
        // Highlight selected item
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, android.R.color.holo_blue_light)
            )
            holder.textView.setTextColor(
                ContextCompat.getColor(holder.itemView.context, android.R.color.white)
            )
        } else {
            holder.itemView.background = ContextCompat.getDrawable(
                holder.itemView.context,
                android.R.drawable.list_selector_background
            )
            val typedArray = holder.itemView.context.obtainStyledAttributes(
                intArrayOf(android.R.attr.textColorPrimary)
            )
            val textColor = typedArray.getColor(0, 0)
            typedArray.recycle()
            holder.textView.setTextColor(textColor)
        }
    }

    override fun getItemCount(): Int = items.size

    fun setSelectedPosition(position: Int) {
        val previousSelected = selectedPosition
        selectedPosition = position
        if (previousSelected != -1) {
            notifyItemChanged(previousSelected)
        }
        if (selectedPosition != -1) {
            notifyItemChanged(selectedPosition)
        }
    }

    fun getSelectedPosition(): Int = selectedPosition
}
