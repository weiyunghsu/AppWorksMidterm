package com.weiyung.publisher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.weiyung.publisher.databinding.ContentHomeBinding

class HomeAdapter (private val onClickListener: OnClickListener):
    ListAdapter<Content, HomeAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ContentHomeBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val content = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(content)
        }
        holder.bind(content)
    }

    class ViewHolder(private val binding: ContentHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(content: Content) {
            binding.content = content
            binding.executePendingBindings()
        }

    }
    object DiffCallback : DiffUtil.ItemCallback<Content>() {
        override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
            return oldItem.id == newItem.id
        }
    }
    class OnClickListener(val clickListener: (content: Content) -> Unit){
        fun onClick(content: Content) = clickListener(content)
    }

}