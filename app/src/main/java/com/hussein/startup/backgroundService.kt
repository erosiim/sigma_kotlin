package com.hussein.startup


import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Handler
import android.os.IBinder
import android.os.Vibrator
import android.widget.Toast
import java.util.*
import kotlin.math.abs

class backgroundService: Service() {

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    var sensor: Sensor?=null
    var sensorManager: SensorManager?=null
    fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }


    var xold=0.0
    var yold=0.0
    var zold=0.0
    var threadShould=1000.0
    var oldtime:Long=0

    fun onSensorChanged(event: SensorEvent?) {
        sensorManager=getSystemService(Context.SENSOR_SERVICE)as SensorManager
        sensor=sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        var x=event!!.values[0]
        var y=event!!.values[1]
        var z=event!!.values[2]
        var currentTime=System.currentTimeMillis()
        if((currentTime-oldtime)>100){
            var timeDiff=currentTime-oldtime
            oldtime=currentTime
            var speed= abs(x+y+z-xold-yold-zold) /timeDiff*10000
            if(speed>threadShould){
                var v=getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                v.vibrate(500)
                Toast.makeText(applicationContext,"shock me caí gg",Toast.LENGTH_LONG).show()
            }
        }
    }



    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        // Send a notification that service is started
        //toast("Service started.")

        Toast.makeText(applicationContext,"Servicio iniciado", Toast.LENGTH_SHORT).show()
        // Do a periodic task
        mHandler = Handler()
        mRunnable = Runnable { onSensorChanged() }
        mHandler.postDelayed(mRunnable, 5000)

        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        //toast("Service destroyed.")
        Toast.makeText(applicationContext,"Servicio Finalizado", Toast.LENGTH_SHORT).show()
        mHandler.removeCallbacks(mRunnable)
    }

    // Custom method to do a task
    private fun showRandomNumber() {
        val rand = Random()
        val number = rand.nextInt(100)
        //toast("Random Number : $number")
        Toast.makeText(applicationContext,"Random Number : $number", Toast.LENGTH_SHORT).show()

        mHandler.postDelayed(mRunnable, 5000)
    }
}