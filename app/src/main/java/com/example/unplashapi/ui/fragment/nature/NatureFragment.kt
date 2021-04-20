package com.example.unplashapi.ui.fragment.nature

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.unplashapi.R
import com.example.unplashapi.databinding.FragmentNatureBinding
import com.example.unplashapi.models.Image
import dagger.hilt.android.AndroidEntryPoint

// di from nature VM
@AndroidEntryPoint
class NatureFragment : Fragment(R.layout.fragment_nature),
    NatureImageAdapter.OnItemClickListener {

    private val viewModel by viewModels<NatureViewModel>()

    private var _binding: FragmentNatureBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentNatureBinding.bind(view)

        val adapter = NatureImageAdapter(this)

        binding.apply {
            natureRecyclerview.setHasFixedSize(true)
            natureRecyclerview.itemAnimator = null
            natureRecyclerview.adapter = adapter.withLoadStateHeaderAndFooter(
                header = NatureLoadStateAdapter { adapter.retry() },
                footer = NatureLoadStateAdapter { adapter.retry() }
            )
            natureRetryBtn.setOnClickListener { adapter.retry() }
        }

        // paging with viewmodel
        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                // ui visivility
                natureProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                natureRecyclerview.isVisible = loadState.source.refresh is LoadState.NotLoading
                natureText.isVisible = loadState.source.refresh is LoadState.Error
                natureRetryBtn.isVisible = loadState.source.refresh is LoadState.Error

                // empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    natureRecyclerview.isVisible = false
                    natureEmptyText.isVisible = true
                }else{
                    natureEmptyText.isVisible = false
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // fragment action nature to detail
    override fun onItemClick(image: Image) {
        val action = NatureFragmentDirections.actionNatureFragmentToDetailFragment(image)
        findNavController().navigate(action)
    }


}