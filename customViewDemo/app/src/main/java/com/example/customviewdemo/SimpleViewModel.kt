package com.example.customviewdemo

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.view.RoundedCorner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

//The SimpleViewModel
class SimpleViewModel :ViewModel() {

    //The model for the paint app for phase 1 is so simple, it's contained within the viewmodel

    //The MutableLiveData objects are the objects actually being updated during painting while the
    //LiveData objects are observed in the DrawFragment.
    private val _bitmap: MutableLiveData<Bitmap> = MutableLiveData(Bitmap.createBitmap(1440, 2990, Bitmap.Config.ARGB_8888))
    val bitmap = _bitmap as LiveData<Bitmap>
    private val _penColor: MutableLiveData<Color> = MutableLiveData(Color.valueOf(1f, 1f, 0f))
    val penColor = _penColor as LiveData<Color>

}