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
import com.jhiltunen.stepupcounter.utils.SharedPreferencesManager
import kotlinx.coroutines.InternalCoroutinesApi
import kotlin.math.pow


class HomeFragment : Fragment(R.layout.fragment_home), SensorEventListener {

    private val TAG = "HomeFragment"
    @InternalCoroutinesApi
    private lateinit var homeViewModel: HomeViewModel
    private var sensorManager: SensorManager? = null
    private lateinit var tvStepsCount : TextView
    private lateinit var bmiValue : TextView
    private lateinit var circularProgressBar : CircularProgressBar
    private var sharedPreferencesManager = SharedPreferencesManager()

    // count number of steps as float value because progress bar animation needs float values
    private var totalStepsSinceLastRebootOfDevice= 0f
    private var running = false

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // get viewModel
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // get circular progress bar UI element
        circularProgressBar = view.findViewById(R.id.progress_circular)

        // get the SensorManager when creating the view
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // find the TextView element that shows value of users current steps
        tvStepsCount = view.findViewById(R.id.tv_stepsTaken) as TextView
        // find the TextView element that shows value of users bmi
        bmiValue = view.findViewById(R.id.bmiValue)

        // observe users table on database for changes
        // if data on users table changes -> bmi is calculated and updated to UI
        homeViewModel.getUser.observe(viewLifecycleOwner, {
            val bmi = (it.weight / (it.height / 100.0).pow(2))
            // use DecimalFormat to get number with two significant digits
            bmiValue.text = DecimalFormat("##.##").format(bmi)
        })

        // observe users current steps (from database)
        // if value changes -> update new value to UI
        homeViewModel.getUsersStepsCountFromSpecificDate.observe(viewLifecycleOwner, {
            tvStepsCount.text = it.toString()
            // set max value for progressbar
            circularProgressBar.progressMax = 10000f
            // update progressbar animation
            circularProgressBar.apply {
                setProgressWithAnimation(it.toFloat())
            }
        })
        homeViewModel.checkIfDateHasChanged()
    }

    @InternalCoroutinesApi
    override fun onResume() {
        super.onResume()
        homeViewModel.getUsersStepsCountFromSpecificDate.observe(viewLifecycleOwner, {
            tvStepsCount.text = it.toString()
            // set max value for progressbar
            circularProgressBar.progressMax = 16500f
            // update progressbar animation
            circularProgressBar.apply {
                setProgressWithAnimation(it.toFloat())
            }
        })

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
        homeViewModel.checkIfDateHasChanged()
    }

    /**
     * This function is called when sensor value changes/sensor detects movement.
     */
    @InternalCoroutinesApi
    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            // get sensor value from event
            // "!!" makes sure that sensor value is not retrieved from null event
            totalStepsSinceLastRebootOfDevice = event!!.values[0]

            sharedPreferencesManager.saveSensorValueToSharedPreferences(requireContext().applicationContext, totalStepsSinceLastRebootOfDevice)
            //homeViewModel.repeatFun(totalStepsSinceLastRebootOfDevice)

            Log.d(TAG, "Sensorin arvo: $totalStepsSinceLastRebootOfDevice")
            // call method to calculate steps value
            homeViewModel.calculateSteps(totalStepsSinceLastRebootOfDevice)

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