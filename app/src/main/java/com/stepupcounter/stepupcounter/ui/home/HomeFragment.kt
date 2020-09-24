package com.stepupcounter.stepupcounter.ui.home

import android.content.Context
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.stepupcounter.stepupcounter.R
import com.stepupcounter.stepupcounter.utils.SharedPreferencesManager
import com.stepupcounter.stepupcounter.utils.Steps
import org.joda.time.DateTime
import org.joda.time.Days
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment(), SensorEventListener {

    private val TAG = "HomeFragment"
    private lateinit var homeViewModel: HomeViewModel
    private var sharedPreferencesManager : SharedPreferencesManager = SharedPreferencesManager()
    private var sensorManager: SensorManager? = null
    private lateinit var tv_stepsCount : TextView

    private var steps : Steps = Steps()
    // count number os steps as float value because progress bar animation needs float values
    private var totalStepsSinceLastRebootOfDevice= 0f
    private var running = false

    var sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

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

        steps = sharedPreferencesManager.loadData(this.requireActivity().applicationContext)


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

            if (steps.getPreviousSteps() == -1f) {
                steps.setpreviousStepsValue(totalStepsSinceLastRebootOfDevice)
            }

            var currentSteps : Int = totalStepsSinceLastRebootOfDevice.toInt() - steps.getPreviousSteps().toInt()

            if (totalStepsSinceLastRebootOfDevice == 0f) {
                currentSteps = totalStepsSinceLastRebootOfDevice.toInt() + steps.getStepsFromSpecificDate(sdf.format(Date())).toInt()
            }

            Log.d(TAG, "previousStepsValue: " + steps.getPreviousSteps())
            Log.d(
                TAG,
                "currentSteps: $currentSteps"
            )
            tv_stepsCount.text = currentSteps.toString()

            val currentDate = Date()
            val lastDate : Date = sdf.parse(steps.getLastDate())

            val currentDateTime = DateTime(currentDate)
            val lastDateTime = DateTime(lastDate)

            // Joda
            Log.d(
                TAG,
                "currentDate: " + currentDate + "; lastSavedDate: " + lastDate + " date.before(currentdate): " + lastDate.before(
                    currentDate
                )
            )
            // if last date saved to LinkedHashMap is before current date we need to reset steps count becauuse
            if (Days.daysBetween(lastDateTime, currentDateTime).days == 0) {
                Log.d(TAG, "Sama päivä")
                steps.addValue(sdf.format(Date()), currentSteps.toFloat())
                sharedPreferencesManager.saveData(this.requireActivity().applicationContext, steps)
            } else {
                resetSteps()
            }
            Log.d(TAG, "currentSteps: $currentSteps")
            // update progressbar animation
            //progress_circular.apply {
              //  setProgressWithAnimation(currentSteps.toFloat())
                //Log.d(TAG, "setProgressWithAnimation")
            // }
        } else {
            Log.d(TAG, "Event null")
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
        tv_stepsCount.text = 0.toString()
        steps.setpreviousStepsValue(totalStepsSinceLastRebootOfDevice)
        steps.addValue(sdf.format(Date()), 0f)
        // call saveData to save it to SharedPreferences
        sharedPreferencesManager.saveData(this.requireActivity().applicationContext, steps)

        // when clicked the element that holds value of steps, show message how to reset the value
        tv_stepsCount.setOnClickListener {
            Toast.makeText(
                this.requireActivity().applicationContext,
                "Long tap to reset steps",
                Toast.LENGTH_SHORT
            ).show()
        }

        tv_stepsCount.setOnLongClickListener {
            tv_stepsCount.text = 0.toString()
            steps.setpreviousStepsValue(totalStepsSinceLastRebootOfDevice)
            steps.addValue(sdf.format(Date()), 0f)
            // call saveData to save it to SharedPreferences
            sharedPreferencesManager.saveData(this.requireActivity().applicationContext, steps)

            true
        }
    }
}