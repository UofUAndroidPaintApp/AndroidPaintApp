package com.example.customviewdemo

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.fonts.FontFamily
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.customviewdemo.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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

//class MainActivity : ComponentActivity() {
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            PhoneAppTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
//
//                    Column {
//                        if (user == null) {
//                            Column {
//                                var email by remember{ mutableStateOf("") }
//                                var password by remember{ mutableStateOf("") }
//                                Text("Not logged in")
//                                OutlinedTextField(value = email, onValueChange = {email = it}, label = { Text("Email") })
//                                OutlinedTextField(value = password, onValueChange = {password = it},
//                                    label = { Text("Password") }, visualTransformation = PasswordVisualTransformation()
//                                )
//
//                                Row{
//                                    Button(onClick ={
//                                        Firebase.auth.signInWithEmailAndPassword(email, password)
//                                            .addOnCompleteListener(this@MainActivity){task->
//                                                if(task.isSuccessful){
//                                                    user = Firebase.auth.currentUser
//                                                } else {
//                                                    email = "login failed, try again"
//                                                }
//                                            }
//                                    }){
//                                        Text("Log In")
//                                    }
//                                    Button(onClick = {
//                                        Firebase.auth.createUserWithEmailAndPassword(email, password)
//                                            .addOnCompleteListener(this@MainActivity){ task ->
//                                                if(task.isSuccessful){
//                                                    user = Firebase.auth.currentUser
//                                                } else {
//                                                    email = "Create user failed, try again"
//                                                    Log.e("Create user error", "${task.exception}")
//                                                }
//                                            }
//                                    }){
//                                        Text("Sign Up")
//                                    }
//                                }
//                            }
//
//                        } else {
//                            Text("Welcome ${user!!.email} with id: ${user!!.uid}")
//                            Button(onClick = {
//                                Firebase.auth.signOut()
//                                user = null
//                            }) {
//                                Text("Sign out")
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
//)
//
//val Purple80 = Color(0xFFD0BCFF)
//val PurpleGrey80 = Color(0xFFCCC2DC)
//val Pink80 = Color(0xFFEFB8C8)
//
//val Purple40 = Color(0xFF6650a4)
//val PurpleGrey40 = Color(0xFF625b71)
//val Pink40 = Color(0xFF7D5260)
//
//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//)
//
//@Composable
//fun PhoneAppTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
//    content: @Composable () -> Unit
//) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.primary.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
//        }
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography,
//        content = content
//    )
//}