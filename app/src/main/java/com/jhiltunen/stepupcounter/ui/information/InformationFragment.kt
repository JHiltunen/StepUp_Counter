package com.jhiltunen.stepupcounter.ui.information

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jhiltunen.stepupcounter.R
import com.jhiltunen.stepupcounter.data.models.User
import com.jhiltunen.stepupcounter.utils.SharedPreferencesManager
import kotlinx.android.synthetic.main.activity_user_info_popup.*
import kotlinx.coroutines.InternalCoroutinesApi

class InformationFragment : Fragment(R.layout.fragment_information) {

    @InternalCoroutinesApi
    private lateinit var informationViewModel: InformationViewModel
    private var sharedPreferenceManager = SharedPreferencesManager()

    private lateinit var username : EditText
    private lateinit var height : EditText
    private lateinit var weight : EditText
    private lateinit var genderRadioGroup: RadioGroup

    private lateinit var saveBtn : Button

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        informationViewModel = ViewModelProvider(this).get(InformationViewModel::class.java)

        username = view.findViewById(R.id.et_username)
        height = view.findViewById(R.id.heightan)
        weight = view.findViewById(R.id.weightan)
        genderRadioGroup = view.findViewById(R.id.gender)
        saveBtn = view.findViewById(R.id.save)

        //informationViewModel.loadUserInformation()

        informationViewModel.getUser.observe(viewLifecycleOwner, {
            Log.d("TAG", "onViewCreated: $it")
            Log.d("TAG", "onViewCreated gender: ${it.gender}")

            username.setText(it.username)
            height.setText(it.height.toString())
            weight.setText(it.weight.toString())
            if (it.gender == "Male") {
                genderRadioGroup.check(R.id.male)
            } else {
                genderRadioGroup.check(R.id.female)
            }
        })

        saveBtn.setOnClickListener {
            var gender : String
            if (genderRadioGroup.checkedRadioButtonId == male.id) {
                gender = "Male"
            } else {
                gender = "Female"
            }
            informationViewModel.updateUser(User(sharedPreferenceManager.loadUserId(requireContext()), username.text.toString(), Integer.valueOf(height.text.toString()), Integer.valueOf(weight.text.toString()), gender))
        }
    }
}