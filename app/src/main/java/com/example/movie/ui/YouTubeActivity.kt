package com.example.movie.ui

import android.os.Bundle
import android.widget.Toast
import com.example.movie.R
import com.example.moviesapp.util.Constants.Companion.TRAILER
import com.example.moviesapp.util.Constants.Companion.YOUTUBE_API_KEY
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_you_tube.*

class YouTubeActivity : YouTubeBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_you_tube)

        val yKey = intent.getStringExtra(TRAILER)


        ytPlayer.initialize(YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                wasRestored: Boolean
            ) {
                if (!wasRestored) {
                    player?.loadVideo(yKey)
                }
                player?.play()
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(this@YouTubeActivity, "Video player Failed", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}
