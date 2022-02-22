package com.example.movie.ui.fragments

import android.os.Bundle
import android.view.*
import android.view.View.VISIBLE
import android.widget.Toast
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
import com.example.movie.util.Resource
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



        viewModel.mostPopularMovies.observe(viewLifecycleOwner, { response ->
            when(response) {
                is Resource.Success -> {
                    hidePopularShimmer()
                    response.data?.let { response ->
                        mostPapularAdapter.differ.submitList(response.results)
                        recyclerViewPopular.apply {
                            adapter = mostPapularAdapter
                            setHasFixedSize(true)
                        }
                    }
                }
                is Resource.Error -> {
                    hidePopularShimmer()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG).show()
                        showError(message)
                    }
                }
                is Resource.Loading -> {
                    showPopularShimmer()
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

        viewModel.topRatedMovies.observe(viewLifecycleOwner, { response ->
            when(response) {
                is Resource.Success -> {
                    hideTopRatedShimmer()
                    response.data?.let { response ->
                        topRatedAdapter.differ.submitList(response.results)
                        recyclerViewTopRated.apply {
                            adapter = topRatedAdapter
                            setHasFixedSize(true)
                        }
                    }
                }
                is Resource.Error -> {
                    hideTopRatedShimmer()
                    response.message?.let { message ->
                        showError(message)
                    }
                }
                is Resource.Loading -> {
                    showTopRatedShimmer()
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

        viewModel.upComingMovies.observe(viewLifecycleOwner, { response ->
            when(response) {
                is Resource.Success -> {
                    hideUpComingShimmer()
                    hideError()
                    response.data?.let { response ->
                        upComingAdapter.differ.submitList(response.results)
                        recyclerViewUpComing.apply {
                            adapter = upComingAdapter
                            setHasFixedSize(true)
                        }
                    }
                }
                is Resource.Error -> {
                    hideUpComingShimmer()
                    response.message?.let { message ->
                        showError(message)                    }
                }
                is Resource.Loading -> {
                     showUpComingShimmer()
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


        btnRetry.setOnClickListener {
            viewModel.getDataAgain()
        }
        setHasOptionsMenu(true)
    }


    private fun showUpComingShimmer() {
         shimmerFrameLayoutUpComing.startShimmer()
         shimmerFrameLayoutUpComing.visibility = VISIBLE
         recyclerViewUpComing.visibility = View.GONE
    }
    private fun hideUpComingShimmer() {
        shimmerFrameLayoutUpComing.stopShimmer()
        shimmerFrameLayoutUpComing.visibility = View.GONE
        recyclerViewUpComing.visibility = VISIBLE
    }

    private fun showTopRatedShimmer() {
         shimmerFrameLayoutTopRated.startShimmer()
         shimmerFrameLayoutTopRated.visibility = VISIBLE
         recyclerViewTopRated.visibility = View.GONE
    }
    private fun hideTopRatedShimmer() {
         shimmerFrameLayoutTopRated.stopShimmer()
         shimmerFrameLayoutTopRated.visibility = View.GONE
         recyclerViewTopRated.visibility = VISIBLE
    }

    private fun showPopularShimmer() {
         shimmerFrameLayoutPopular.startShimmer()
         shimmerFrameLayoutPopular.visibility = VISIBLE
         recyclerViewPopular.visibility = View.GONE
    }
    private fun hidePopularShimmer() {
         shimmerFrameLayoutPopular.stopShimmer()
         shimmerFrameLayoutPopular.visibility = View.GONE
         recyclerViewPopular.visibility = VISIBLE
    }


    private fun showError(error:String){
        textViewError.visibility=VISIBLE
        textViewError.text=error
        btnRetry.visibility=VISIBLE
    }
    private fun hideError(){
        textViewError.visibility=View.GONE
        btnRetry.visibility=View.GONE
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

    override fun onPause() {
          shimmerFrameLayoutPopular.stopShimmer()
          shimmerFrameLayoutTopRated.stopShimmer()
          shimmerFrameLayoutUpComing.stopShimmer()
        super.onPause()
    }


}