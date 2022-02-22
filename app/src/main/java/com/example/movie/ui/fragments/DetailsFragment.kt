package com.example.movie.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager

import com.example.movie.R
import com.example.movie.adapter.AdapterCast
import com.example.movie.data.trailer.Trailer
import com.example.movie.ui.MainViewModel
import com.example.movie.ui.YouTubeActivity
import com.example.moviesapp.util.Constants
import com.example.moviesapp.util.Constants.Companion.TRAILER
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject


@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    lateinit var myActionBar: ActionBar

    @Inject
    lateinit var castAdapter:AdapterCast


    lateinit var trailerKey:String

    @Inject
    lateinit var glide: RequestManager

    private val viewModel by viewModels<MainViewModel>()
    
    val args : DetailsFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myActionBar=(activity as AppCompatActivity).supportActionBar !!
        myActionBar?.elevation = 0F

        viewModel.movieCast.observe(viewLifecycleOwner,{response ->
            response.body()?.let {
                castAdapter.differ.submitList(it.cast)
                recyclerViewCast.apply {
                    adapter = castAdapter
                    setHasFixedSize(true)
                }
            }

        })

        castAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("cast", it)
            }
            findNavController().navigate(
                R.id.action_detailsFragment_to_castDetailsFragment,
                bundle
            )
        }

        val movie = args.movie
        viewModel.getCast(movie.id)
        viewModel.getTrailer(movie.id)

        myActionBar?.title=movie.original_title
        myActionBar?.subtitle="details"
        glide.load(Constants.IMAGE_URL+movie.poster_path).into(poster_path)
        original_title.text=movie.original_title
        release_date.text=movie.release_date
        vote_average_rating.text=""+movie.vote_average+" / 10"
        rating_movie.rating= movie.vote_average.toFloat()
        overview.text=movie.overview

        glide.load(Constants.IMAGE_URL+movie.backdrop_path).into(backdrop_path)

        viewModel.trailer.observe(viewLifecycleOwner,{response->
            response.body()?.let {
                if (it.results.isNotEmpty() ){
                    trailerKey=it.results[0].key
                }
            }

        })

        btn_trailer.setOnClickListener {
            val intent = Intent(activity, YouTubeActivity::class.java)
            intent.putExtra(TRAILER, trailerKey)
            startActivity(intent)
        }
        


        if (movie.adult){
            adults.text="This movie for adults only."
            adults.setTextColor(Color.RED)
        }else{
            adults.text="This movie is suitable for all ages."
            adults.setTextColor(Color.GREEN)
        }

        original_language.text=movie.original_language




    }


    override fun onDestroy() {
        super.onDestroy()
        myActionBar?.subtitle=""
    }


    override fun onStop() {
        super.onStop()
        myActionBar?.subtitle=""

    }

    
}