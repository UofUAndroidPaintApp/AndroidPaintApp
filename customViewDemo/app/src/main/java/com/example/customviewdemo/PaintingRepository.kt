package com.example.customviewdemo

import android.R.attr
import android.content.Context
import android.graphics.Bitmap
import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.prefs.Preferences


//The "Repository" is basically the "in-memory" part of the model
class PaintingRepository(val scope: CoroutineScope, val dao: PaintingDAO, context: Context) {

    //    val currentPainting = dao.latestPainting().asLiveData()
//    val allPaintings = dao.allPaintings().asLiveData()
    suspend fun addPainting(bitmap: Bitmap) {

    }
}

