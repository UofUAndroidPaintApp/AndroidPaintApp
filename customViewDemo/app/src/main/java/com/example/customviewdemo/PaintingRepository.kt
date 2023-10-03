package com.example.customviewdemo

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date


//The "Repository" is basically the "in-memory" part of the model
class PaintingRepository (val scope: CoroutineScope, val dao: PaintingDAO) {

    val currentPainting = dao.latestPainting().asLiveData()
    val allPaintings = dao.allPaintings().asLiveData()
    fun addPainting(bitmap: Bitmap){
        scope.launch {
//            dao.addPaintingData(
//                PaintingData(Date(), filename = "", bitmap))
        }
    }
}