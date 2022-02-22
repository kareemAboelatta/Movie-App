package com.example.movie.ui.fragments

import android.os.Bundle
import android.view.*
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.movie.adapter.AdapterPopularMovie
import com.example.movie.adapter.AdapterTopRatedAndUpComing
import com.example.movie.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel by viewModels<MainViewModel>()
    @Inject
    lateinit var mostPapularAdapter: AdapterPopularMovie
    @Inject
    lateinit var topRatedAdapter: AdapterTopRatedAndUpComing
    @Inject
    lateinit var upComingAdapter: AdapterTopRatedAndUpComing


    @Inject
    lateinit var glide: RequestManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //actionBar


        viewModel.mostPopularMovies.observe(viewLifecycleOwner,{response ->
            hidePopularShimmer()
            response.body()?.let { response ->
                mostPapularAdapter.differ.submitList(response.results)
                recyclerViewPopular.apply {
                    adapter = mostPapularAdapter
                    setHasFixedSize(true)
                }
            }
        })

        mostPapularAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
            }
            findNavController().navigate(
                R.id.action_mainFragment_to_detailsFragment,
                bundle
            )
        }

        viewModel.topRatedMovies.observe(viewLifecycleOwner,{response ->
            hideTopRatedShimmer()
            response.body()?.let { response ->
                topRatedAdapter.differ.submitList(response.results)
                recyclerViewTopRated.apply {
                    adapter = topRatedAdapter
                    setHasFixedSize(true)
                }
            }
        })
        topRatedAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
            }
            findNavController().navigate(
                R.id.action_mainFragment_to_detailsFragment,
                bundle
            )
        }
        viewModel.upComingMovies.observe(viewLifecycleOwner,{response ->
            hideUpComingShimmer()
            response.body()?.let { response ->
                upComingAdapter.differ.submitList(response.results)
                recyclerViewUpComing.apply {
                    adapter = upComingAdapter
                    setHasFixedSize(true)
                }
            }
        })
        upComingAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("movie", it)
            }
            findNavController().navigate(
                R.id.action_mainFragment_to_detailsFragment,
                bundle
            )
        }

        setHasOptionsMenu(true)
    }


    private fun hideUpComingShimmer() {
         shimmerFrameLayoutUpComing.stopShimmer()
         shimmerFrameLayoutUpComing.visibility = View.GONE
         recyclerViewUpComing.visibility = View.VISIBLE
    }
    private fun hideTopRatedShimmer() {
         shimmerFrameLayoutTopRated.stopShimmer()
         shimmerFrameLayoutTopRated.visibility = View.GONE
         recyclerViewTopRated.visibility = View.VISIBLE
    }
    private fun hidePopularShimmer() {
         shimmerFrameLayoutPopular.stopShimmer()
         shimmerFrameLayoutPopular.visibility = View.GONE
         recyclerViewPopular.visibility = VISIBLE
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.movies_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController()
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
          shimmerFrameLayoutPopular.startShimmer()
          shimmerFrameLayoutTopRated.startShimmer()
          shimmerFrameLayoutUpComing.startShimmer()


    }

    override fun onStart() {
        super.onStart()
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.elevation = 0F
        actionBar?.title="Recommended movies"

    }

    override fun onPause() {
          shimmerFrameLayoutPopular.stopShimmer()
          shimmerFrameLayoutTopRated.stopShimmer()
          shimmerFrameLayoutUpComing.stopShimmer()
        super.onPause()
    }


}