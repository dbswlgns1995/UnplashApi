package com.example.unplashapi.ui.fragment.nature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.unplashapi.databinding.ItemLoadStateBinding


// error -> alternative retry btn, text load
class NatureLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<NatureLoadStateAdapter.NatureLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NatureLoadStateAdapter.NatureLoadStateViewHolder {
        val binding = ItemLoadStateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return NatureLoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NatureLoadStateAdapter.NatureLoadStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    inner class NatureLoadStateViewHolder(private val binding: ItemLoadStateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.itemRetryBtn.setOnClickListener {
                retry.invoke()
            }
        }
        
        fun bind(loadState: LoadState) {
            binding.apply {
                itemProgressBar.isVisible = loadState is LoadState.Loading
                itemRetryBtn.isVisible = loadState !is LoadState.Loading
                itemErrorText.isVisible = loadState !is LoadState.Loading
            }
        }
    }

}