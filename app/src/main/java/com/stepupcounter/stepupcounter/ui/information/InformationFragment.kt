package com.stepupcounter.stepupcounter.ui.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.stepupcounter.stepupcounter.R

class InformationFragment : Fragment() {

    private lateinit var informationViewModel: InformationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        informationViewModel =
            ViewModelProvider(this).get(InformationViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_information, container, false)

        return root
    }
}