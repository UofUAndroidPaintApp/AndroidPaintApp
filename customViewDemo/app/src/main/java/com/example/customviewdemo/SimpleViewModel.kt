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

    private val _penColor: MutableLiveData<Color> = MutableLiveData(Color.valueOf(1f, 1f, 0f))
    val penColor = _penColor as LiveData<Color>

    fun pickColor() {
        _penColor.value = Color.valueOf(2F, 2F, 0F)
    }

}