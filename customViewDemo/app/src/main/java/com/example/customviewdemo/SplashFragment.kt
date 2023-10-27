package com.example.customviewdemo

import android.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
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

        //From Gloria
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    splashCompose()
                }
            }
            Handler(Looper.myLooper()!!).postDelayed({
                findNavController().navigate(R.id.loginFragment)
            }, 1500L)
        }
    }
}

//From Gloria
@Composable
fun splashCompose() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.go_team),
            contentDescription = "The image of the team",
            modifier = Modifier.fillMaxWidth()
        )
    }
}