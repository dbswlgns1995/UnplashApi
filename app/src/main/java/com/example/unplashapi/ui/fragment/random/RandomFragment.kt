package com.example.unplashapi.ui.fragment.random

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.unplashapi.R
import com.example.unplashapi.databinding.FragmentRandomBinding
import com.example.unplashapi.models.Image
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomFragment : Fragment(R.layout.fragment_random),
    RandomImageAdapter.OnItemClickListener {

    private val viewModel by viewModels<RandomViewModel>()

    private var _binding: FragmentRandomBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentRandomBinding.bind(view)

        val adapter = RandomImageAdapter(this)

        binding.apply {
            randomRecyclerview.setHasFixedSize(true)
            randomRecyclerview.itemAnimator = null
            randomRecyclerview.adapter = adapter.withLoadStateHeaderAndFooter(
                header = RandomLoadStateAdapter { adapter.retry() },
                footer = RandomLoadStateAdapter { adapter.retry() }
            )
            randomRetryBtn.setOnClickListener { adapter.retry() }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                randomProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                randomRecyclerview.isVisible = loadState.source.refresh is LoadState.NotLoading
                randomText.isVisible = loadState.source.refresh is LoadState.Error
                randomRetryBtn.isVisible = loadState.source.refresh is LoadState.Error

                // empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    randomRecyclerview.isVisible = false
                    randomEmptyText.isVisible = true
                }else{
                    randomEmptyText.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_random, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    binding.randomRecyclerview.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(image: Image) {
        val action = RandomFragmentDirections.actionRandomFragmentToDetailFragment(image)
        findNavController().navigate(action)
    }


}