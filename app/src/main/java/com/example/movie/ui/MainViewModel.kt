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

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract

import com.example.movie.util.Resource
import java.io.IOException


class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val context: Context
    ) : ViewModel() {

    private val _mostPopularMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val mostPopularMovies: LiveData<Resource<MovieResponse>> get() = _mostPopularMovies

    private val _topRatedMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val topRatedMovies: LiveData<Resource<MovieResponse>> get() = _topRatedMovies

    private val _upComingMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val upComingMovies: LiveData<Resource<MovieResponse>> get() = _upComingMovies

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
            safeMostPopularCall()
            safeTopRatedCall()
            safeUpComingCall()
        }
    }


    fun getDataAgain(){
        viewModelScope.launch {
            safeMostPopularCall()
            safeTopRatedCall()
            safeUpComingCall()
        }
    }

    private suspend fun safeMostPopularCall() {
        _mostPopularMovies.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = repository.getMovies(POPULAR,STARTING_POSITION)
                _mostPopularMovies.postValue(handleMostPopularResponse(response))
            } else {
                _mostPopularMovies.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> _mostPopularMovies.postValue(Resource.Error("Network Failure"))
                else -> _mostPopularMovies.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
    private suspend fun safeTopRatedCall() {
        _topRatedMovies.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = repository.getMovies(TOP_RATED,STARTING_POSITION)
                _topRatedMovies.postValue(handleTopRatedResponse(response))
            } else {
                _topRatedMovies.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> _topRatedMovies.postValue(Resource.Error("Network Failure"))
                else -> _topRatedMovies.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
    private suspend fun safeUpComingCall() {
        _upComingMovies.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = repository.getMovies(UPCOMING,STARTING_POSITION)
                _upComingMovies.postValue(handleUpComingResponse(response))
            } else {
                _upComingMovies.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> _upComingMovies.postValue(Resource.Error("Network Failure"))
                else -> _upComingMovies.postValue(Resource.Error("Conversion Error"))
            }
        }
    }



    private fun handleMostPopularResponse(response: Response<MovieResponse>) : Resource<MovieResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleTopRatedResponse(response: Response<MovieResponse>) : Resource<MovieResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleUpComingResponse(response: Response<MovieResponse>) : Resource<MovieResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
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


    //to check internet
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ContactsContract.CommonDataKinds.Email.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}