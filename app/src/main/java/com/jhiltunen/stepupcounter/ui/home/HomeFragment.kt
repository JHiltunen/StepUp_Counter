package com.jhiltunen.stepupcounter.ui.home

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.jhiltunen.stepupcounter.R


class HomeFragment : Fragment(R.layout.fragment_home), SensorEventListener {

    private val TAG = "HomeFragment"
    private lateinit var homeViewModel: HomeViewModel
    private var sensorManager: SensorManager? = null
    private lateinit var tv_stepsCount : TextView
    private lateinit var bmiValue : TextView
    private lateinit var circularProgressBar : CircularProgressBar

    // count number os steps as float value because progress bar animation needs float values
    private var totalStepsSinceLastRebootOfDevice= 0f
    private var running = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        circularProgressBar = view.findViewById(R.id.progress_circular)

        // get the SensorManager when creating the view
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // find the TextView element (that shows value of steps) from root
        tv_stepsCount = view.findViewById(R.id.tv_stepsTaken) as TextView
        tv_stepsCount.text = homeViewModel.stepsCount.toString()
        bmiValue = view.findViewById(R.id.bmiValue)
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.updateStepsCount()
        tv_stepsCount.text = homeViewModel.stepsCount.toString()

        homeViewModel.calculateBodyMassIndex()
        bmiValue.text = DecimalFormat("##.##").format(homeViewModel.bmi)

        running = true
        // get the Step Counter sensor from sensormanager
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            // step sensor not found on some (older) devices
            // we need to use requireActivity() method to access context that's needed on Toast
            Toast.makeText(
                this.requireActivity().applicationContext,
                "No sensor detected on this device",
                Toast.LENGTH_LONG
            ).show()
            Log.d(TAG, "Sensor not found")
        } else {
            // add listener for the sensor we use
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
            Log.d(TAG, "Added listener")
        }
    }

    /**
     * This function is called when sensor value changes/sensor detects movement.
     * Calculating currentSteps by taking totalStepsSinceLastRebootOfDevice and subtracting previousTotalSteps from it.
     * previousTotalSteps is retrieved from sharedPreferences when onCreateView function at the start is called.
     * Calls circular progressbar setProgressAnimation to visualize step count.
     */
    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            // event!! -> event not null
            // value of sensor is at index 0
            totalStepsSinceLastRebootOfDevice = event!!.values[0]

            Log.d(TAG, "Sensorin arvo: $totalStepsSinceLastRebootOfDevice")

            homeViewModel.calculateSteps(totalStepsSinceLastRebootOfDevice)
            tv_stepsCount.text = homeViewModel.stepsCount.toString()

            circularProgressBar.progressMax = 16500f
            // update progressbar animation
            circularProgressBar.apply {
                setProgressWithAnimation(tv_stepsCount.text.toString().toFloat())
            }

        } else {
            Log.d(TAG, "Event null")
        }
    }

    /**
     * Not used on this project.
     */
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}