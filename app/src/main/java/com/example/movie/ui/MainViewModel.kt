package com.example.movie.ui

import android.content.Context
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.Repository
import com.example.movie.data.cast.CastResponse
import com.example.movie.data.MovieResponse
import com.example.movie.data.castdetails.CastDetailsResponse
import com.example.movie.data.trailer.TrailerResponse
import com.example.moviesapp.util.Constants.Companion.API_KEY
import com.example.moviesapp.util.Constants.Companion.POPULAR
import com.example.moviesapp.util.Constants.Companion.STARTING_POSITION
import com.example.moviesapp.util.Constants.Companion.TAG
import com.example.moviesapp.util.Constants.Companion.TOP_RATED
import com.example.moviesapp.util.Constants.Companion.UPCOMING
import kotlinx.coroutines.launch
import retrofit2.Response
import androidx.core.content.ContextCompat.getSystemService

import android.net.ConnectivityManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.InetAddress


class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    @ApplicationContext val context: Context
    ) : ViewModel() {



    private val _mostPopularMovies: MutableLiveData<Response<MovieResponse>> = MutableLiveData()
    val mostPopularMovies: LiveData<Response<MovieResponse>> get() = _mostPopularMovies


    private val _topRatedMovies: MutableLiveData<Response<MovieResponse>> = MutableLiveData()
    val topRatedMovies: LiveData<Response<MovieResponse>> get() = _topRatedMovies


    private val _upComingMovies: MutableLiveData<Response<MovieResponse>> = MutableLiveData()
    val upComingMovies: LiveData<Response<MovieResponse>> get() = _upComingMovies

    private var _movieCast: MutableLiveData<Response<CastResponse>> = MutableLiveData()
    val movieCast: LiveData<Response<CastResponse>> get() = _movieCast


    private var _castDetails: MutableLiveData<Response<CastDetailsResponse>> = MutableLiveData()
    val castDetails: LiveData<Response<CastDetailsResponse>> get() = _castDetails

    private var _trailer: MutableLiveData<Response<TrailerResponse>> = MutableLiveData()
    val trailer: LiveData<Response<TrailerResponse>> get() = _trailer

    private val _searchMovies: MutableLiveData<Response<MovieResponse>> = MutableLiveData()
    val searchMovies: LiveData<Response<MovieResponse>> get() = _searchMovies




    init {

        viewModelScope.launch {
            val mostPopularMovies =repository.getMovies(POPULAR,STARTING_POSITION)
            val topRatedMovies =repository.getMovies(TOP_RATED,STARTING_POSITION)
            val upComingMovies =repository.getMovies(UPCOMING,STARTING_POSITION)
            _mostPopularMovies.value=mostPopularMovies
            _topRatedMovies.value=topRatedMovies
            _upComingMovies.value=upComingMovies


        }

    }

    fun getCast(id: Int){
        viewModelScope.launch {
            val cast =repository.getMovieCredits(id)
            _movieCast.value=cast
        }
    }

    fun getCastDetails(PersonId: Int){
        viewModelScope.launch {
            val castDetials =repository.getCastDetails(PersonId)
            _castDetails.value=castDetials
        }
    }

    fun getTrailer(id: Int){
        viewModelScope.launch {
            try {
                val response = repository.getMovieTrailers(id = id, API_KEY)
                _trailer.postValue(response)
            } catch (e: Exception) {
                Log.d(TAG, "error occurred ${e.printStackTrace()}")
            }
        }
    }

    fun getSearchMovie(querySearch: String){
        viewModelScope.launch {
            val response = repository.getSearchMovies(querySearch,API_KEY,STARTING_POSITION)
            _searchMovies.value=response

        }
    }


    private fun hasInternetConnection(): Boolean {
        return isNetworkConnected() && isInternetAvailable()
    }


    private fun isNetworkConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            val ipAddr: InetAddress = InetAddress.getByName("google.com")
            //You can replace it with your name
            !ipAddr.equals("")
        } catch (e: java.lang.Exception) {
            false
        }
    }
}