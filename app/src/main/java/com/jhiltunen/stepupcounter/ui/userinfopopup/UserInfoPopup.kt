package com.jhiltunen.stepupcounter.ui.userinfopopup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jhiltunen.stepupcounter.MainActivity
import com.jhiltunen.stepupcounter.R
import com.jhiltunen.stepupcounter.data.models.User
import kotlinx.android.synthetic.main.activity_user_info_popup.*
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class UserInfoPopup : AppCompatActivity() {

    @InternalCoroutinesApi
    private lateinit var userInfoPopupViewModel: UserInfoPopupViewModel

    private var username : String = ""
    private var weight : Int = 0
    private var height : Int = 0
    private var gender : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info_popup)
        userInfoPopupViewModel = ViewModelProvider(this).get(UserInfoPopupViewModel::class.java)

        createUserProfile_btn.setOnClickListener{
            addUserToDB()
        }
    }

    private fun addUserToDB() {
        // get text from editTexts
        username = et_username.text.toString()
        height = Integer.valueOf(et_height.text.toString())
        weight = Integer.valueOf(et_weight.text.toString())

        if (genderGroup.checkedRadioButtonId == male.id) {
            gender = "Male"
        } else {
            gender = "Female"
        }

        //Check that the form is complete before submitting data to the database
        if (!(username.isEmpty() || height == 0 || weight == 0 || gender.isEmpty())) {
            val user = User(0, username, height, weight, gender)

            userInfoPopupViewModel.adduser(user)

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(applicationContext, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }
    }
}