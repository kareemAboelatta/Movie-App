package com.example.movie.data.cast

data class CastResponse(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)