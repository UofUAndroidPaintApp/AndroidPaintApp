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

    private var _width = 1
    private var _height = 1

    //Model
    private val _bitmap: MutableLiveData<Bitmap> = MutableLiveData(Bitmap.createBitmap(1440, 2990, Bitmap.Config.ARGB_8888))
    val bitmap = _bitmap as LiveData<Bitmap>
    private val _color : MutableLiveData<Color> = MutableLiveData(Color.valueOf(1f, 1f, 0f))
    public val color  = _color as LiveData<Color>
    private val _shape: MutableLiveData<Paint.Cap> = MutableLiveData(Paint.Cap.ROUND)
    public val shape = _shape as LiveData<Paint.Cap>



    fun pickColor(){
        with(Random.Default) {
            _color.value = Color.valueOf(nextFloat(), nextFloat(), nextFloat())
        }
    }

    fun makeBitMap(width: Int, height: Int) {
        if (width == _width && _height == height) {
            return
        }
        _bitmap.value = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        _height = height
        _width = width
    }

}