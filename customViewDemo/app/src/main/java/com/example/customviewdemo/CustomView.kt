package com.example.customviewdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var bitmap = Bitmap.createBitmap(1440, 20, Bitmap.Config.ARGB_8888)
    private var bitmapCanvas = Canvas(bitmap)
    private val paint = Paint()
    private val rect: Rect by lazy {Rect(0,0,width, height)}
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bitmap, null, rect, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        super.onTouchEvent(event)

        val x = event!!.x.toInt()
        val y = event!!.y.toInt()

        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                bitmapCanvas.drawCircle(x.toFloat(), y.toFloat(), 10f, paint)
            }
            MotionEvent.ACTION_MOVE -> {
                bitmapCanvas.drawCircle(x.toFloat(), y.toFloat(), 10f, paint)
            }
        }

        Log.d("circle", "circle has been drawn at $x and $y")



        invalidate()
        return true

//        return super.onTouchEvent(event)
    }



    public fun drawCircle(color: Color){
        paint.color = Color.WHITE
        bitmapCanvas.drawRect(0f,0f, bitmap.width.toFloat(), bitmap.height.toFloat(), paint)
        paint.color = color.toArgb()
        bitmapCanvas.drawCircle(0.5f*bitmap.width, 0.5f*bitmap.height,
            0.25f*bitmap.width, paint)
        Log.d("color", "color is...")
        invalidate()
    }

    fun setBitMap(bitmap: Bitmap){
        this.bitmap  = bitmap
        this.bitmapCanvas = Canvas(bitmap)
    }

}