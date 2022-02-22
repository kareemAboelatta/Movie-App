package com.example.movie.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.RequestManager
import com.example.movie.R
import com.example.moviesapp.util.Constants.Companion.IMAGE_URL
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




    }

    override fun onResume() {
        super.onResume()
        val navController = findNavController(R.id.nav_host_frag)
        NavigationUI.setupActionBarWithNavController(this,navController)
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_frag)
        navController.navigateUp()
        return true
    }

}