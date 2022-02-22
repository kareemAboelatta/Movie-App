package com.example.movie.di

import com.example.movie.Repository
import com.example.movie.api.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMovieRepository(movieService: MovieService): Repository{
        return Repository(movieService)
    }
}