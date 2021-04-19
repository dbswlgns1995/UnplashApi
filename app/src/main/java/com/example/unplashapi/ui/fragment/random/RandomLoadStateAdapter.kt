package com.example.unplashapi.ui.fragment.random

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.unplashapi.databinding.ItemLoadStateBinding

class RandomLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<RandomLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RandomLoadStateAdapter.LoadStateViewHolder {
        val binding = ItemLoadStateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RandomLoadStateAdapter.LoadStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    class LoadStateViewHolder(private val binding : ItemLoadStateBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(loadState: LoadState) {
            binding.apply {
                itemProgressBar.isVisible = loadState is LoadState.Loading
                itemRetryBtn.isVisible = loadState !is LoadState.Loading
                itemErrorText.isVisible = loadState !is LoadState.Loading
            }
        }
    }

}