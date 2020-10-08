package com.jhiltunen.stepupcounter.ui.information

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jhiltunen.stepupcounter.R
import com.jhiltunen.stepupcounter.data.models.BodyMassIndex
import com.jhiltunen.stepupcounter.data.models.User
import com.jhiltunen.stepupcounter.utils.SharedPreferencesManager
import kotlinx.android.synthetic.main.activity_user_info_popup.*
import kotlinx.coroutines.InternalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow

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
        // get viewModel
        informationViewModel = ViewModelProvider(this).get(InformationViewModel::class.java)

        // get UI elements
        username = view.findViewById(R.id.et_username)
        height = view.findViewById(R.id.heightan)
        weight = view.findViewById(R.id.weightan)
        genderRadioGroup = view.findViewById(R.id.gender)
        saveBtn = view.findViewById(R.id.save)

        // observe users table on database for changes
        // if data on users table changes -> InformationFragment UI is updated
        informationViewModel.getUser.observe(viewLifecycleOwner, {
            Log.d("TAG", "onViewCreated: $it")
            Log.d("TAG", "onViewCreated gender: ${it.gender}")

            // changed data retrieved from database
            // set text for the UI elements
            username.setText(it.username)
            height.setText(it.height.toString())
            weight.setText(it.weight.toString())
            // gender is saved as String to database
            // so we can't (because we don't have id of radioButton) directly check correct radioButton
            if (it.gender == "Male") {
                genderRadioGroup.check(R.id.male)
            } else {
                genderRadioGroup.check(R.id.female)
            }
        })

        // when user clicks save button
        saveBtn.setOnClickListener {
            var gender : String
            if (genderRadioGroup.checkedRadioButtonId == male.id) {
                gender = "Male"
            } else {
                gender = "Female"
            }

            if (username.text.isEmpty() || weight.text.isEmpty() || height.text.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                var weightAsInt : Int = Integer.valueOf(weight.text.toString())
                var heightAsInt : Int = Integer.valueOf(height.text.toString())

                var dataIsValid: Boolean

                if (weightAsInt > 500) {
                    Toast.makeText(context, "Weight should be 0-500", Toast.LENGTH_SHORT).show()
                    dataIsValid = false
                } else {
                    dataIsValid = true
                }

                if (heightAsInt > 250) {
                    Toast.makeText(context, "Height should be 0-250", Toast.LENGTH_SHORT).show()
                    dataIsValid = false
                } else {
                    dataIsValid = true
                }

                if (dataIsValid) {
                    var bmi = (weightAsInt.toFloat() / (heightAsInt.toFloat() / 100.0).pow(2))
                    Log.d("BMI", "onViewCreated: BMI-> $bmi")
                    // update users data
                    informationViewModel.updateUser(User(sharedPreferenceManager.loadUserId(requireContext()),
                        username = username.text.toString(),
                        height = heightAsInt,
                        weight = weightAsInt,
                        gender = gender
                    ))
                    informationViewModel.addBodyMassIndex(BodyMassIndex(
                        id = 0,
                        date = SimpleDateFormat("yyyy-MM-dd").format(Date()),
                        bodyMassIndex = bmi,
                        userId = sharedPreferenceManager.loadUserId(requireContext())
                    ))
                }
            }
        }
    }
}