package com.example.customviewdemo.network

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ServerViewModel(val repository: ServerRepository) : ViewModel() {

    // LiveData for storing the current image
    private val imageLiveData: LiveData<Bitmap?> = MutableLiveData(null)


    // Create a new image from the provided bitmap and save it to the device
    suspend fun createNewImage(context: Context): String? {
        val bitmap = imageLiveData.value ?: return null
        // Save the image and post it to the server
        val imagePath = saveImage(bitmap, context)
        postNewPaint(Firebase.auth.currentUser?.uid ?: "", imagePath)
        return imagePath
    }

    // Post a new painting to the server with user ID and image path
    private suspend fun postNewPaint(userId: String, imagePath: String) {
        repository.postNewPaint(userId, imagePath)
    }

    // Fetch paintings created by the current user
    suspend fun getCurrentUserPaints(userId: String) {
        repository.getCurrentUserPaints(userId)
    }

    // Fetch all posts from all users
    suspend fun getAllPostsFromAllUsers() {
        repository.getAllPostsFromAllUsers()
    }

    // Delete a painting by its ID
    fun deletePaintById(id: Int) {
        repository.deletePaintById(id)
    }

    // Get the current date and time as a formatted string
    fun getCurrentDateTimeString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.getDefault())
        val currentTime = Date()
        return dateFormat.format(currentTime)
    }

    // Save a bitmap as an image file on the device
    fun saveImage(bitmap: Bitmap, context: Context): String {
        val imageFileName = "${getCurrentDateTimeString()}.png"
        val imageFile =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageFileName)

        imageFile.outputStream().use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }

        val imageUri = imageFile.toUri().toString()
//        Log.d("MainPage", "Image saved to $imageUri")
        Toast.makeText(context, "Image was saved successfully", Toast.LENGTH_LONG).show()
        return imageUri
    }

}




class ServerViewModelFactory(private val repository: ServerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

