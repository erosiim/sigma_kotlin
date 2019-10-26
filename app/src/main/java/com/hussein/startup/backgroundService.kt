package com.hussein.startup


import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.IBinder
import android.os.Vibrator
import android.widget.Toast
import java.util.*
import kotlin.math.abs

class backgroundService: Service(){


    private var sensorManager: SensorManager?=null
    private var lastUpdate: Long = 0


    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private var listen : SensorListen?=null



    var xold=0.0
    var yold=0.0
    var zold=0.0
    var threadShould=1850.0
    var oldtime:Long=0

    override fun onBind(intent: Intent): IBinder? {
        return null;
    }
    override fun onCreate() {
        // TODO Auto-generated method stub
        Toast.makeText(applicationContext, "Started", Toast.LENGTH_SHORT ).show()
        super.onCreate()
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        // To display thread
        //mSensorManager = (SensorManager)getSystemService( SENSOR_SERVICE );
        //mSensor = mSensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );
        //MySensorListener msl = new MySensorListener();

        //sensorManager.registerListener(msl, mSensor, SensorManager.SENSOR_DELAY_UI);


        //sensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        lastUpdate = System.currentTimeMillis()

        sensorManager=getSystemService(Context.SENSOR_SERVICE)as SensorManager
        var sensor: Sensor=sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        listen= SensorListen()
        sensorManager!!.registerListener(listen , sensor,SensorManager.SENSOR_DELAY_UI)




        return Service.START_STICKY

        // Send a notification that service is started
        //toast("Service started.")

        //Toast.makeText(applicationContext,"Servicio iniciado", Toast.LENGTH_SHORT).show()
        // Do a periodic task
        //mHandler = Handler()
        //mRunnable = Runnable { onSensorChanged() }
        //mHandler.postDelayed(mRunnable, 5000)

        //return Service.START_STICKY
    }

    override fun onDestroy() {
        // TODO Auto-generated method stub
        sensorManager?.unregisterListener(listen)
        Toast.makeText(this, "Destroy", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }
    // Custom method to do a task
    private fun showRandomNumber() {
        val rand = Random()
        val number = rand.nextInt(100)
        //toast("Random Number : $number")
        Toast.makeText(applicationContext,"Random Number : $number", Toast.LENGTH_SHORT).show()

        mHandler.postDelayed(mRunnable, 5000)
    }

    inner class SensorListen : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent) {
           //Toast.makeText(applicationContext,"acelerometro", Toast.LENGTH_SHORT).show()
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

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // TODO Auto-generated method stub

        }

    }

    }

