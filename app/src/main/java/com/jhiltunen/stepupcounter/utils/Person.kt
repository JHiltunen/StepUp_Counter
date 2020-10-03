package com.jhiltunen.stepupcounter.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class Person {

    private var height : Int = 0
    private var weight : Int = 0
    private var gender : Int = 0
    private var daysAndSteps: LinkedHashMap<String, Float> = LinkedHashMap<String, Float>()
    private var previousStepsValue : Float = -1f

    init {
        daysAndSteps.put(SimpleDateFormat("yyyy-MM-dd").format(Date()), 0f)
    }

    fun setHeight(height: Int) {
        this.height = height
    }

    fun setWeight(weight: Int) {
        this.weight = weight
    }

    fun setGender(gender: Int) {
        this.gender = gender
    }

    fun getHeight(): Int {
        return this.height
    }

    fun getWeight(): Int {
        return this.weight
    }

    fun getGender(): Int {
        return this.gender
    }

    fun addValue(date: String, value: Float) {
        daysAndSteps.put(date, value)
    }

    fun setpreviousStepsValue(stepCount: Float) {
        previousStepsValue = stepCount
    }

    fun getPreviousSteps() : Float {
        return previousStepsValue
    }

    fun setPreviousStepsValueToLastSavedValue(stepsValue: Float) {
        previousStepsValue = stepsValue
    }

    fun getStepsFromSpecificDate(date: String) : Float {
        return daysAndSteps.getOrDefault(date, 0f)
    }

    fun getLastDate(): String {
        val keys : MutableSet<String> = daysAndSteps.keys
        return keys.last()
    }

    fun getDaysAndSteps() : LinkedHashMap<String, Float> {
        return daysAndSteps
    }

    fun getDaysAsArray(): Array<String> {
        return daysAndSteps.keys.toTypedArray()
    }

    fun getStepsAsList(): ArrayList<Float> {
        val values: Collection<Float> = daysAndSteps.values
        return ArrayList(values)
    }

    override fun toString(): String {
        return "Person(daysAndSteps=$daysAndSteps, previousStepsValue=$previousStepsValue)"
    }


}