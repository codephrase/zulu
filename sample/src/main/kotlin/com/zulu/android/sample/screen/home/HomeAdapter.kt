package com.zulu.android.sample.screen.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zulu.android.sample.R

class HomeAdapter(
    private val items: List<Item>,
    private val itemClickListener: ((item: Item) -> Unit)? = null
) : RecyclerView.Adapter<HomeViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]

        return when (item.type) {
            ItemType.SECTION -> SECTION_ITEM
            else -> DEFAULT_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layoutId = when (viewType) {
            SECTION_ITEM -> R.layout.item_home_section
            else -> R.layout.item_home_default
        }

        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

        val viewHolder = HomeViewHolder(view)
        viewHolder.setOnClickListener { position, type ->
            val item = items[position]
            itemClickListener?.invoke(item)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = items[position]

        holder.textViewTitle.text = item.title
    }

    companion object {
        private const val DEFAULT_ITEM = 0
        private const val SECTION_ITEM = 1
    }
}

class HomeViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {
    val textViewTitle: TextView = view.findViewById(R.id.text_view_title)

    fun setOnClickListener(listener: (position: Int, viewType: Int) -> Unit) {
        itemView.setOnClickListener {
            listener.invoke(adapterPosition, itemViewType)
        }
    }
}