package com.example.customviewdemo

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
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
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Phase 2, part 2 - action bar
        setSupportActionBar(findViewById(R.id.toolbar))

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerViewID) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

    }
    //Phase 2, part 2 - action bar
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}





