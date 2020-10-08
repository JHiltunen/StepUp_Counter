package com.jhiltunen.stepupcounter.ui.userinfopopup

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jhiltunen.stepupcounter.MainActivity
import com.jhiltunen.stepupcounter.R
import com.jhiltunen.stepupcounter.data.models.Steps
import com.jhiltunen.stepupcounter.data.models.User
import com.jhiltunen.stepupcounter.utils.SharedPreferencesManager
import kotlinx.android.synthetic.main.activity_user_info_popup.*
import kotlinx.coroutines.InternalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

/**
 * UserInfoPopup will be shown at app first launch.
 * User will be asked to choose username and provide height, weight and gender.
 */
@InternalCoroutinesApi
class UserInfoPopup : AppCompatActivity() {

    @InternalCoroutinesApi
    private lateinit var userInfoPopupViewModel: UserInfoPopupViewModel
    private var sharedPreferencesManager = SharedPreferencesManager()

    private var username : String = ""
    private var weight : Int = 0
    private var height : Int = 0
    private var gender : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info_popup)
        // get viewModel
        userInfoPopupViewModel = ViewModelProvider(this).get(UserInfoPopupViewModel::class.java)

        // set onClickListener to save button
        createUserProfile_btn.setOnClickListener{
            // onClick call function to save new user to database
            addUserToDB()
        }
    }

    /**
     * Saves new user to database.
     */
    private fun addUserToDB() {
        // get text from editTexts
        username = et_username.text.toString()
        if (et_height.text.isNotEmpty() && et_weight.text.isNotEmpty()) {
            height = Integer.valueOf(et_height.text.toString())
            weight = Integer.valueOf(et_weight.text.toString())
        }

        // set user gender by comparing checked radio button id
        if (genderGroup.checkedRadioButtonId == male.id) {
            gender = "Male"
        } else {
            gender = "Female"
        }

        //Check that the form is complete (no empty fields) before submitting data to the database
        if (!(username.isEmpty() || height == 0 || weight == 0 || gender.isEmpty())) {
            // create new user object
            val user = User(0, username, height, weight, gender)

            // add user to database
            userInfoPopupViewModel.addUser(user)
            sharedPreferencesManager.saveIsFirstLaunch(applicationContext, false)
            // create new intent to start MainActivity again
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            // call finnish function to prevent navigating back to same view and close this activity
            finish()
        } else {
            Toast.makeText(applicationContext, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }
    }
}