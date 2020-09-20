package com.stepupcounter.stepupcounter.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.stepupcounter.stepupcounter.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), SensorEventListener {

    private lateinit var homeViewModel: HomeViewModel
    private var sensorManager: SensorManager? = null
    private lateinit var tv_stepsCount : TextView

    // count number os steps as float value because progress bar animation needs float values
    private var totalStepsSinceLastRebootOfDevice= 0f
    private var previousTotalSteps = 0f
    private var running = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        // get the SensorManager when creating the view
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // find the TextView element (that shows value of steps) from root
        tv_stepsCount = root.findViewById(R.id.tv_stepsTaken) as TextView

        loadData()
        resetSteps()

        return root
    }

    override fun onResume() {
        super.onResume()
        running = true
        // get the Step Counter sensor from sensormanager
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            // step sensor not found on some (older) devices
            // we need to use requireActivity() method to access context that's needed on Toast
            Toast.makeText(this.requireActivity().applicationContext, "No sensor detected on this device", Toast.LENGTH_LONG).show()
            Log.d("Steps", "Sensor not found")
        } else {
            // add listener for the sensor we use
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
            Log.d("Steps", "Added listener")
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

            // previousTotalSteps is retrieved from sharedPreferences on loadData function when creating the view
            // currentSteps = totalStepsSinceLastRebootOfDevice - previousTotalSteps
            val currentSteps : Int = totalStepsSinceLastRebootOfDevice.toInt() - previousTotalSteps.toInt()
            Log.d(
                "Steps",
                "totalStepsSinceLastRebootOfDevice: $totalStepsSinceLastRebootOfDevice : currentSteps: $currentSteps"
            )
            tv_stepsCount.text = currentSteps.toString()

            Log.d("Steps", "currentSteps: $currentSteps")
            // update progressbar animation
            progress_circular.apply {
                setProgressWithAnimation(currentSteps.toFloat())
                Log.d("Steps", "setProgressWithAnimation")
            }
        } else {
            Log.d("Steps", "Event null")
        }
    }

    /**
     * Not used on this project.
     */
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    /**
     * Fuction to reset steps to zero (0).
     * Assigns onClickListener to tv_stepsCount TextView to show message, how to reset steps.
     * Assign onLongClickListener to actually reset the value and then save the data to shared preferences by calling saveData() function.
     */
    private fun resetSteps() {
        // when clicked the element that holds value of steps, show message how to reset the value
        tv_stepsCount.setOnClickListener {
            Toast.makeText(this.requireActivity().applicationContext, "Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }

        tv_stepsCount.setOnLongClickListener {
            previousTotalSteps = totalStepsSinceLastRebootOfDevice
            tv_stepsCount.text = 0.toString()
            // call saveData to save it to SharedPreferences
            saveData()

            true
        }
    }

    /**
     * Loads the number of steps saved to shared preferences with the key "key1"
     * Name of sharedPrefrences is "myPrefs"
     */
    private fun loadData() {
        // loads steps count from shared preferences
        val sharedPrefrences : SharedPreferences = this.requireActivity().getSharedPreferences(
            "myPrefs",
            Context.MODE_PRIVATE
        )
        val savedNumber : Float = sharedPrefrences.getFloat("key1", 0f)
        Log.d("Steps", "$savedNumber")
        previousTotalSteps = savedNumber
    }

    /**
     * Saves the number of steps in the shared preferences with the key "key1"
     * Name of sharedPrefrences is "myPrefs"
     */
    private fun saveData() {
        val sharedPrefrences : SharedPreferences = this.requireActivity().getSharedPreferences(
            "myPrefs",
            Context.MODE_PRIVATE
        )
        val editor : SharedPreferences.Editor = sharedPrefrences.edit()
        editor.putFloat("key1", previousTotalSteps)
        editor.apply()
        Log.d("Steps", "saved data")
    }
}