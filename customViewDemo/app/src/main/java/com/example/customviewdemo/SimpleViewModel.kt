package com.example.customviewdemo

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.view.RoundedCorner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class SimpleViewModel :ViewModel() {

    //Model
    private val _bitmap: MutableLiveData<Bitmap> = MutableLiveData(Bitmap.createBitmap(1440, 2990, Bitmap.Config.ARGB_8888))
    val bitmap = _bitmap as LiveData<Bitmap>

}