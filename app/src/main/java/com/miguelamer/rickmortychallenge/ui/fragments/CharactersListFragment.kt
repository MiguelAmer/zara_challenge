package com.miguelamer.rickmortychallenge.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miguelamer.rickmortychallenge.Constants.Companion.QUERY_PAGE_SIZE
import com.miguelamer.rickmortychallenge.Constants.Companion.SEARCH_DELAY
import com.miguelamer.rickmortychallenge.R
import com.miguelamer.rickmortychallenge.adapters.CharactersAdapter
import com.miguelamer.rickmortychallenge.databinding.FragmentCharactersListBinding
import com.miguelamer.rickmortychallenge.repository.CharactersRepository
import com.miguelamer.rickmortychallenge.ui.CharactersViewModel
import com.miguelamer.rickmortychallenge.ui.CharactersViewModelProviderFactory
import com.miguelamer.rickmortychallenge.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CharactersListFragment: Fragment() {

    companion object {
        const val EXTRA_CHARACTER = "EXTRA_CHARACTER"
    }
    lateinit var viewModel: CharactersViewModel
    lateinit var charactersAdapter: CharactersAdapter
    lateinit var binding: FragmentCharactersListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharactersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = CharactersRepository()
        val viewModelProviderFactory = CharactersViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), viewModelProviderFactory).get(CharactersViewModel::class.java)
        setupRecyclerView()
        charactersAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable(EXTRA_CHARACTER, it)
            }
            findNavController().navigate(
                R.id.action_charactersListFragment_to_characterDetailFragment,
                bundle
            )
        }
        handleCharacterSearch()
        handleViewModelResponse()
    }

    private fun handleViewModelResponse() {
        viewModel.characters.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideLoading()
                    response.data?.let {
                        charactersAdapter.differ.submitList(response.data.results.toList())
                        val totalPages = response. data.info.pages
                        isLastPage = (viewModel.charactersPage == totalPages) || (viewModel.charactersSearchPage == totalPages)
                    }
                }
                is Resource.Error -> {
                    hideLoading()
                    response.message?.let { message ->
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showLoading()
                }
            }
        })
    }

    private fun handleCharacterSearch() {
        var job: Job? = null
        binding.edittextSearch.addTextChangedListener { text ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_DELAY)
                text?.let {
                    viewModel.charactersSearchPage = 1
                    viewModel.searchCharacters(text.toString())
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        isLoading = false
    }

    private fun setupRecyclerView() {
        charactersAdapter = CharactersAdapter()
        binding.charactersRecyclerView.apply {
            adapter = charactersAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(customScrollListener)
        }
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val customScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            isScrolling = newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL

        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeggining = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisibleItems = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeggining && isTotalMoreThanVisibleItems && isScrolling
            if (shouldPaginate) {
                if (viewModel.charactersSearchPage > 1) {
                    viewModel.searchCharacters(binding.edittextSearch.text.toString())
                } else {
                    viewModel.getCharacters()
                }
            }
        }
    }


}