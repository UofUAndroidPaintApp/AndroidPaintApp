package com.example.customviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.customviewdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerViewID) as NavHostFragment
        navController = navHostFragment.navController

//        binding.fragmentContainerView.getFragment<ClickFragment>().setButtonFunction {
//            val drawFragment = DrawFragment()
//            val transaction = this.supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.fragmentContainerView, drawFragment, "draw_tag")
//            transaction.addToBackStack(null)
//            transaction.commit()
//        }
        setContentView(binding.root)
    }
}