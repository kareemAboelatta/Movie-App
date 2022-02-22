package com.example.movie

import com.example.movie.api.MovieService
import com.example.moviesapp.util.Constants

class Repository  (
     private val apiService: MovieService
) {
     suspend fun getMovies(query: String,page: Int) = apiService.getMovies(query, Constants.API_KEY,page)
     suspend fun getMovieCredits(id: Int) =
          apiService.getMovieCredits(id = id,Constants.API_KEY)

     suspend fun getCastDetails(id: Int) =
          apiService.getCastDetail(personId = id,Constants.API_KEY)

     suspend fun getMovieTrailers(id: Int, apiKey: String) =
          apiService.getMovieTrailers(id = id, apiKey = apiKey)

     suspend fun getSearchMovies(querySearch: String, apiKey: String,page: Int) =
          apiService.searchMovies(querySearch = querySearch, apiKey = apiKey, page = page)



}
