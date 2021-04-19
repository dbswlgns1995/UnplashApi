package com.example.unplashapi.ui.fragment.random

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.unplashapi.R
import com.example.unplashapi.databinding.ItemImageBinding
import com.example.unplashapi.models.Image

class RandomImageAdapter :
    PagingDataAdapter<Image, RandomImageAdapter.ImageViewHolder>(Image_COMPARATOR) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    class ImageViewHolder(private val binding : ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {

        /*
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }*/

        fun bind(image: Image) {
            binding.apply {
                Glide.with(itemView)
                    .load(image.urls.full)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageViewItem)
            }
        }
    }

    companion object {
        private val Image_COMPARATOR = object : DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Image, newItem: Image) =
                oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(image: Image)
    }
}