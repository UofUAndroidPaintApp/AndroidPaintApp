package com.example.customviewdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.getSystemService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var bitmap = Bitmap.createBitmap(1440, 20, Bitmap.Config.ARGB_8888)
    private var bitmapCanvas = Canvas(bitmap)
    private val paint = Paint()
    private val rect: Rect by lazy {Rect(0,0,width, height)}
    var offset: Float = 15F

    //Phase 3 - We need a sensor manager to be able to access the gravity sensor
    private lateinit var sensorManager: SensorManager
    private lateinit var gravity: Sensor

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


    //Phase 3 - initialize the gravoty sensor
    fun initializeSensor(){
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)!!
    }

    //Phase 3 - Updated method from Ben's gyroscope example
    fun getGravData(): Flow<FloatArray>{
        return channelFlow {
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event !== null) {
                        Log.e("Sensor event!", event.values.toString())
                        var success = channel.trySend(event.values.copyOf()).isSuccess
                        Log.e("success?", success.toString())
                    }
                    else
                    {
                        Log.e("Sensor event!", "NO SENSOR")
                    }
                }
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                }
            }
            sensorManager.registerListener(listener, gravity, SensorManager.SENSOR_DELAY_GAME)

            awaitClose {
                sensorManager.unregisterListener(listener)
            }
        }
    }

    fun moveMarble(floatX: Float, floatY: Float) {
        //loop the animations forever
       // val outerInfinite = rememberInfiniteTransition(label = "outerInfinite")
        //this is the fraction of the screen that the box will cover, we'll just interpolate between
        // .2 and 1.0 of the screen size forever

            //This x and y will the the coordinates of the marble
            var x = floatX
            var y = floatY


            //Compare the gravity and marble coordinates
            if (floatX > 0) {
                x += 3
            }
            if (floatX < 0) {
                x -= 3
            }
            if (floatY > 0) {
                y += 3
            }
            if (floatY < 0) {
                y -= 3
            }

            if (x < 0){
                x = 0f
            }
            if (y < 0) {
                y = 0f
            }

        if(y -this.bitmapCanvas.height < 2){
            y += 20
        }
        if(x - this.bitmapCanvas.height < 2){
            x+=20
        }

        val paint = Paint()

        bitmapCanvas.drawCircle(x,y,20f,paint)
        invalidate()
    }


}