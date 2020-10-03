package com.stepupcounter.stepupcounter.ui.information

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.stepupcounter.stepupcounter.R

class InformationFragment : Fragment(R.layout.fragment_information) {

    private lateinit var informationViewModel: InformationViewModel

    private lateinit var height : TextView
    private lateinit var weight : TextView
    private lateinit var genderRadioGroup: RadioGroup

    private lateinit var saveBtn : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        informationViewModel = ViewModelProvider(this).get(InformationViewModel::class.java)

        height = view.findViewById(R.id.heightan)
        weight = view.findViewById(R.id.weightan)
        genderRadioGroup = view.findViewById(R.id.gender)
        saveBtn = view.findViewById(R.id.save)

        informationViewModel.loadUserInformation()

        height.text = informationViewModel.height.toString()
        weight.text = informationViewModel.weight.toString()
        genderRadioGroup.check(informationViewModel.gender)

        saveBtn.setOnClickListener {
            informationViewModel.height = Integer.valueOf(height.text.toString())
            informationViewModel.weight = Integer.valueOf(weight.text.toString())
            informationViewModel.gender = genderRadioGroup.checkedRadioButtonId
            informationViewModel.saveUserInformation()
        }
    }
}