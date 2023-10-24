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
    public var offset: Float = 15F
    //Enum for the paint tool pen tip shapes
    enum class Shape {
        circle,rectangle
    }
    var shape = Shape.circle

    //The onDraw() method creates a canvas that can be painted on
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bitmap, null, rect, paint)
    }

    //The onTouchEvent() method manages the actually "painting" via mouse movements on the canvas
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        super.onTouchEvent(event)
        val x = event!!.x.toInt()
        val y = event!!.y.toInt()

        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                if (shape == Shape.circle) {
                    bitmapCanvas.drawCircle(x.toFloat(), y.toFloat(), offset, paint)
                }
                else {
                    bitmapCanvas.drawRect(x.toFloat(), y.toFloat(), x.toFloat()+ offset , y.toFloat()+ offset, paint)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (shape == Shape.circle) {
                    bitmapCanvas.drawCircle(x.toFloat(), y.toFloat(), offset, paint)
                }
                else {
                    bitmapCanvas.drawRect(x.toFloat(), y.toFloat(), x.toFloat()+ offset, y.toFloat()+ offset, paint)
                }
            }
        }

        invalidate()
        return true
    }

    fun setBitMap(bitmap: Bitmap){
        this.bitmap  = bitmap
        this.bitmapCanvas = Canvas(bitmap)
    }
    fun setColor(color: Int) {
        this.paint.color = color
    }

}