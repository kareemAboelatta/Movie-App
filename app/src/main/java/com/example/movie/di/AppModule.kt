package com.example.movie.di

import android.content.Context
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.movie.R
import com.example.movie.Repository
import com.example.movie.api.MovieService
import com.example.moviesapp.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideTestString1() = "This is a string we will inject"

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context) = context


    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.color.blackColor )
            .error(R.drawable.ic_baseline_image_24)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )





}