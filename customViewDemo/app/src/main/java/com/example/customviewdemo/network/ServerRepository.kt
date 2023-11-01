package com.example.customviewdemo.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ServerRepository(val scope: CoroutineScope, val serverService: ServerService) {

    var currentUserPaintsHistory: MutableLiveData<List<PaintResponse>> =
        MutableLiveData(listOf())

    // LiveData to hold all paints from all users
    var allPaints: MutableLiveData<List<PaintResponse>> =
        MutableLiveData(listOf())

    // LiveData to hold the current paint's title
    var paintTitle: MutableLiveData<String?> = MutableLiveData("Untitled")

    // Get all posts (paints) from all users
    suspend fun getAllPostsFromAllUsers() {
        val allPaintsFromUsers = serverService.getAllPaints()
        allPaints.postValue(allPaintsFromUsers)
    }


    // Post a new paint to the server
    suspend fun postNewPaint(file: String, imagePath: String) {
//        serverService.postNewPaint(
//            PaintPost(
//                userID, paintTitle.value ?: "Untitled", imagePath
//            )
//        )
        Log.d("ServerRepository", "postNewPaint: ${paintTitle.value ?: "Untitled"}")
    }

    // Get paints created by the current user
    suspend fun getCurrentUserPaints(userId: String) {
        scope.launch {
            val paints = serverService.getCurrentUserPaints(userId)
            Log.d("ServerRepository", "getCurrentUserPaints: $paints")
            currentUserPaintsHistory.postValue(paints)
        }
    }
    // Delete a paint by its ID
    fun deletePaintById(id: Int) {
        scope.launch {
            serverService.deleteAPaintById(id)
        }
    }

}