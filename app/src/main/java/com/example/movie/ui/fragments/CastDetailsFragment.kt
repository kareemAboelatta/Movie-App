package com.example.movie.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.movie.ui.MainViewModel
import com.example.moviesapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cast_details.*
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject

@AndroidEntryPoint
class CastDetailsFragment : Fragment(R.layout.fragment_cast_details) {

    @Inject
    lateinit var glide: RequestManager

    private val viewModel by viewModels<MainViewModel>()
    val args : CastDetailsFragmentArgs by navArgs()


    lateinit var myActionBar: ActionBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myActionBar=(activity as AppCompatActivity).supportActionBar !!
        myActionBar?.elevation = 0F

        val cast = args.cast
        viewModel.getCastDetails(cast.id)

        castCharacter.text="("+cast.character+")"

        viewModel.castDetails.observe(viewLifecycleOwner,{response->
            response.body()?.let {actor->

                myActionBar?.title=actor.name
                myActionBar?.subtitle="details"
                castName.text=actor.name
                castBio.text=actor.biography
                castBirthday.text=actor.birthday
                glide.load(Constants.IMAGE_URL+actor.profile_path).into(castImage)
                castPlace_of_birth.text=actor.place_of_birth
                castPopularity.text=""+actor.popularity
            }
        })
    }



}

