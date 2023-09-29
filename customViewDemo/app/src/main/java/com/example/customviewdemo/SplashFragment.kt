package com.example.customviewdemo

import android.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

//The SplashFragment stores the splash image, manages how long the splash image is displayed as well
//as transitions to the ClickFragment via navigation control
class SplashFragment : androidx.fragment.app.Fragment() {
    //The onCreateView() method manages the navigation from the SplashFragment screen to the
    //ClickFragment welcome screen.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_splash, container, false)
        Handler(Looper.myLooper()!!).postDelayed({
            findNavController().navigate(R.id.action_splashFragmentRealOne_to_clickFragment)
        }, 1500L)
        return view
    }
}