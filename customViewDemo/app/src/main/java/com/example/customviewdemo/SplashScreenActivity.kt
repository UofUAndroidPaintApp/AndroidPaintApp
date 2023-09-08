package com.example.customviewdemo
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)  // Set the content view to the splash screen layout
        Handler().postDelayed({
            // launch the main activity
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            finish()
        }, 1000L)
    }
}

