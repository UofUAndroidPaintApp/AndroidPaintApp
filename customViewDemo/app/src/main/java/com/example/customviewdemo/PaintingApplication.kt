package com.example.customviewdemo

import android.app.Application
import com.example.customviewdemo.network.ServerRepository
import com.example.customviewdemo.network.ServerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class PaintingApplication: Application() {
    val scope = CoroutineScope(SupervisorJob())
    val db by lazy {PaintingDatabase.getDatabase(applicationContext)}
    val paintingRepository by lazy {PaintingRepository(scope, db.paintingDao(), applicationContext)}

}