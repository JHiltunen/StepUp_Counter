package com.stepupcounter.stepupcounter.ui.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.stepupcounter.stepupcounter.R
import kotlinx.android.synthetic.main.fragment_information.*

class InformationFragment : Fragment() {

    private lateinit var informationViewModel: InformationViewModel

    private lateinit var height : TextView
    private lateinit var weight : TextView
    private lateinit var genderRadioGroup: RadioGroup

    private lateinit var saveBtn : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        informationViewModel =
            ViewModelProvider(this).get(InformationViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_information, container, false)

        height = root.findViewById(R.id.heightan)
        weight = root.findViewById(R.id.weightan)
        genderRadioGroup = root.findViewById(R.id.gender)
        saveBtn = root.findViewById(R.id.save)

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

        return root
    }
}