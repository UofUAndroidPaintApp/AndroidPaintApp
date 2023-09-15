package com.example.customviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.customviewdemo.databinding.ActivityMainBinding

//The MainActivity runs at the launch of the drawing app and initiates navigation throughout the app.
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerViewID) as NavHostFragment
        navController = navHostFragment.navController
        setContentView(binding.root)
    }
}