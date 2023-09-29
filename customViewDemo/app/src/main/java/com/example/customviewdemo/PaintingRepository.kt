package com.example.customviewdemo

import android.graphics.Bitmap
import androidx.lifecycle.asLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.random.Random


//The "Repository" is basically the "in-memory" part of the model
class PaintingRepository (val scope: CoroutineScope, val dao: PaintingDAO) {

    val currentPainting = dao.latestPainting().asLiveData()
    val allPaintings = dao.allPaintings().asLiveData()
    fun checkPaintings(painting: Bitmap){
        scope.launch {
            delay(1000) // pretend this is a slow network call
            dao.addPaintingData(
                PaintingData(Date(), filename = ""))
        }
    }
}