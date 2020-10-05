package com.jhiltunen.stepupcounter.ui.information

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jhiltunen.stepupcounter.R
import kotlinx.coroutines.InternalCoroutinesApi

class InformationFragment : Fragment(R.layout.fragment_information) {

    @InternalCoroutinesApi
    private lateinit var informationViewModel: InformationViewModel

    private lateinit var height : TextView
    private lateinit var weight : TextView
    private lateinit var genderRadioGroup: RadioGroup

    private lateinit var saveBtn : Button

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        informationViewModel = ViewModelProvider(this).get(InformationViewModel::class.java)

        height = view.findViewById(R.id.heightan)
        weight = view.findViewById(R.id.weightan)
        genderRadioGroup = view.findViewById(R.id.gender)
        saveBtn = view.findViewById(R.id.save)

        //informationViewModel.loadUserInformation()

        informationViewModel.getUser.observe(viewLifecycleOwner, {
            Log.d("TAG", "onViewCreated: $it")
            Log.d("TAG", "onViewCreated gender: ${it.gender}")
            height.text = it.height.toString()
            weight.text = it.weight.toString()
            if (it.gender == "Male") {
                genderRadioGroup.check(R.id.male)
            } else {
                genderRadioGroup.check(R.id.female)
            }
        })

        /*saveBtn.setOnClickListener {
            informationViewModel.height = Integer.valueOf(height.text.toString())
            informationViewModel.weight = Integer.valueOf(weight.text.toString())
            informationViewModel.gender = genderRadioGroup.checkedRadioButtonId
            informationViewModel.saveUserInformation()
        }*/
    }
}