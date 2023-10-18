package com.example.customviewdemo

import android.content.Context
import android.util.Log
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date


//The "Repository" is basically the "in-memory" part of the model
class PaintingRepository(val scope: CoroutineScope, val dao: PaintingDAO, context: Context) {

    //    val currentPainting = dao.latestPainting().asLiveData()
 val allPics = dao.allPaintings().asLiveData()
    suspend fun addPainting(fileName: String) {
        scope.launch{
            dao.addPaintingData(PaintingData(Date(), fileName))
        }
        Log.d("PaintingRepository", "in addPainting")
    }


    // Gloria: added this method to remove the paint
    suspend fun removePainting(filename: String) {
        dao.removePainting(filename)
    }
}

