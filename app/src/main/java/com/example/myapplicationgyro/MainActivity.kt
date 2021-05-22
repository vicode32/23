@file:Suppress("PrivatePropertyName", "UNUSED_VARIABLE")

package com.example.myapplicationgyro
import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Half.EPSILON
import android.widget.TextView
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


@Suppress("unused")
class MainActivity : AppCompatActivity() , SensorEventListener {
    private lateinit var sensorManager:SensorEvent
    private lateinit var sensor: Sensor
    private val NS2S = 1.0f / 1000000000.0
    private val deltaRotationVector = FloatArray(4) {0f}
    private var timestamp: Float = 0f
    private  lateinit var gyro: TextView
    private lateinit var reg: TextView
    private lateinit var dev: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_NORMAL)


    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
        if(timestamp != 0f && event != null) {
            val dT = (event.timestamp - timestamp) * NS2S
            var axisX: Float = event.values[0]
            var axisY: Float = event.values[1]
            var axisZ: Float = event.values[2]
            val omegaMagnitude: Float = sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ)
         

            if (omegaMagnitude > EPSILON) {
                axisX /= omegaMagnitude
                axisY /= omegaMagnitude
                axisZ /= omegaMagnitude
            }
              val thetaOverTwo: Double = omegaMagnitude * dT / 2.0f
            val sinThetaOverTwo: Double = sin(thetaOverTwo)
            val cosThetaOverTwo: Double = cos(thetaOverTwo)
            deltaRotationVector[0] = (sinThetaOverTwo * axisX).toFloat()
            deltaRotationVector[1] = (sinThetaOverTwo * axisY).toFloat()
            deltaRotationVector[2] = (sinThetaOverTwo * axisZ).toFloat()
            deltaRotationVector[3] = cosThetaOverTwo.toFloat()

            gyro.text = "$omegaMagnitude"
            dev.text = "x =$axisX" + "y = $axisY" + "z =$axisZ"
            








        }
      timestamp = event?.timestamp?.toFloat() ?: 0f
      val deltaRotationMatrix = FloatArray(9) {0f}
      SensorManager.getRotationMatrixFromVector(deltaRotationMatrix,deltaRotationVector)




    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}

private fun SensorManager.registerListener(mainActivity: MainActivity, defaultSensor: Sensor?, sensorDelayNormal: Int) {

}
