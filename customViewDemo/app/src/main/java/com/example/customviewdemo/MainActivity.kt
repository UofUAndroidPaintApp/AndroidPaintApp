package com.example.customviewdemo

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.customviewdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


//The MainActivity runs at the launch of the drawing app and initiates navigation throughout the app.
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm: PaintingViewModel by viewModels { PaintingViewModelFactory((application as PaintingApplication).paintingRepository) }

        val binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerViewID) as NavHostFragment
        navController = navHostFragment.navController
        setContentView(binding.root)
    }
}

data class PaintingResult(var value: Bitmap)
