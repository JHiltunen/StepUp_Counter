package com.stepupcounter.stepupcounter.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.LinkedHashMap

class Steps {

    private var daysAndSteps: LinkedHashMap<String, Float> = LinkedHashMap<String, Float>()
    private var previousStepsValue : Float = -1f

    init {
        daysAndSteps.put(SimpleDateFormat("yyyy-MM-dd").format(Date()), 0f)
    }

    fun addValue(date: String, value: Float) {
        daysAndSteps.put(date, value)
    }

    fun setpreviousStepsValue(stepCount : Float) {
        previousStepsValue = stepCount
    }

    fun getPreviousSteps() : Float {
        return previousStepsValue
    }

    fun setPreviousStepsValueToLastSavedValue(stepsValue : Float) {
        previousStepsValue = stepsValue
    }

    fun getStepsFromSpecificDate(date : String) : Float {
        return daysAndSteps.getOrDefault(date, 0f)
    }

    fun getLastDate(): String {
        val keys : MutableSet<String> = daysAndSteps.keys
        return keys.last()
    }

    fun getDaysAndSteps() : LinkedHashMap<String, Float> {
        return daysAndSteps
    }

    override fun toString(): String {
        return "Steps(daysAndSteps=$daysAndSteps, previousStepsValue=$previousStepsValue)"
    }


}