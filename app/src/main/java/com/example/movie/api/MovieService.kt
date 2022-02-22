package com.example.movie.api

import com.example.movie.data.cast.CastResponse
import com.example.movie.data.MovieResponse
import com.example.movie.data.castdetails.CastDetailsResponse
import com.example.movie.data.trailer.TrailerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/{query}")
    suspend fun getMovies(
        @Path("query") query: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
    ): Response<MovieResponse>

    @GET("movie/{id}/credits")
    suspend fun getMovieCredits(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
    ): Response<CastResponse>


    @GET("person/{person_id}")
    suspend fun getCastDetail(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String
    ): Response<CastDetailsResponse>


    @GET("movie/{id}/videos")
    suspend fun getMovieTrailers(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
    ): Response<TrailerResponse>

    @GET("search/movie/")
    suspend fun searchMovies(
        @Query("query") querySearch: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
    ): Response<MovieResponse>

}