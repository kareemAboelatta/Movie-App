package com.example.movie.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.movie.R
import com.example.movie.adapter.AdapterCast
import com.example.movie.adapter.AdapterSearch
import com.example.movie.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var adapterSearch: AdapterSearch

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    recyclerViewSearch.scrollToPosition(0)
                    searchMovies(query)

                    searchView.clearFocus()
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    recyclerViewSearch.scrollToPosition(0)
                    searchMovies(newText)

                }
                return true
            }
        })



        viewModel.searchMovies.observe(viewLifecycleOwner, {
            it.body()?.let {response->
                adapterSearch.differ.submitList(response.results)
                recyclerViewSearch.apply {
                    adapter = adapterSearch
                    setHasFixedSize(true)
                }
            }
        })


        adapterSearch.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
            }
            findNavController().navigate(
                R.id.action_searchFragment_to_detailsFragment,
                bundle
            )
        }


    }
    private fun searchMovies(query: String) {
        viewModel.getSearchMovie(query)
    }


}