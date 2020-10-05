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
import com.jhiltunen.stepupcounter.data.models.Steps
import kotlinx.coroutines.InternalCoroutinesApi
import kotlin.math.pow


class HomeFragment : Fragment(R.layout.fragment_home), SensorEventListener {

    private val TAG = "HomeFragment"
    @InternalCoroutinesApi
    private lateinit var homeViewModel: HomeViewModel
    private var sensorManager: SensorManager? = null
    private lateinit var tv_stepsCount : TextView
    private lateinit var bmiValue : TextView
    private lateinit var circularProgressBar : CircularProgressBar

    // count number os steps as float value because progress bar animation needs float values
    private var totalStepsSinceLastRebootOfDevice= 0f
    private var running = false

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        circularProgressBar = view.findViewById(R.id.progress_circular)

        // get the SensorManager when creating the view
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // find the TextView element (that shows value of steps) from root
        tv_stepsCount = view.findViewById(R.id.tv_stepsTaken) as TextView
        bmiValue = view.findViewById(R.id.bmiValue)

        homeViewModel.getUser.observe(viewLifecycleOwner, {
            val bmi = (it.weight / (it.height / 100.0).pow(2))
            bmiValue.text = DecimalFormat("##.##").format(bmi)
        })

        homeViewModel.getUsersStepsCountFromSpecificDate.observe(viewLifecycleOwner, {
            tv_stepsCount.text = it.toString()
            circularProgressBar.progressMax = 16500f
            // update progressbar animation
            circularProgressBar.apply {
                setProgressWithAnimation(it.toFloat())
            }
        })
    }

    @InternalCoroutinesApi
    override fun onResume() {
        super.onResume()

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
    @InternalCoroutinesApi
    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            // event!! -> event not null
            // value of sensor is at index 0
            totalStepsSinceLastRebootOfDevice = event!!.values[0]

            Log.d(TAG, "Sensorin arvo: $totalStepsSinceLastRebootOfDevice")
            homeViewModel.calculateStepsDatabase(totalStepsSinceLastRebootOfDevice)

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