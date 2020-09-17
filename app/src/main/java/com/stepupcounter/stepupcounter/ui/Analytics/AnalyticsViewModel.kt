package com.stepupcounter.stepupcounter.ui.Analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AnalyticsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is analytics Fragment"
    }
    val text: LiveData<String> = _text
}