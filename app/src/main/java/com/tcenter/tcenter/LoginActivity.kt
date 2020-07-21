package com.tcenter.tcenter

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import com.tcenter.tcenter.R.array.spinnerItems
import com.tcenter.tcenter.R.id.direct_number_select_spinner
import com.tcenter.tcenter.R.layout.direct_number_spinner
import com.tcenter.tcenter.helper.LoginResponseCode
import com.tcenter.tcenter.service.Login
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private fun redirectToTicketListActivity()
    {
        val intent = Intent(this, TicketListActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        /** LOGIN LAYOUT ELEMENTS */
        val directNumberInput: Spinner    = findViewById(R.id.direct_number_select)
        val phoneNumberInput: EditText    = findViewById(R.id.mobile_number_input)
        val passwordInput: EditText       = findViewById(R.id.password_input)
        val loginBtn: Button              = findViewById(R.id.sign_in_button)
        val showPasswordBtn: ImageButton = findViewById(R.id.show_password_button)
        val hidePasswordBtn: ImageButton  = findViewById(R.id.hide_password_button)

        val ls = Login()

        /**
         * MAKE LOGIN REQUEST ON SIGN IN BUTTON CLICK
         * CHECK CREDENTIALS
         * IF LOGGED SUCCESSFULLY -> CREATE/UPDATE SHARED PREFERENCES FILE
         * IF NOT -> SHOW NOTIFICATION
         */
        try {
            loginBtn.setOnClickListener() {
                val directNumber: String = directNumberInput.selectedItem.toString()
                val phoneNumber: String = phoneNumberInput.text.toString()
                val password: String = passwordInput.text.toString()
                val responseCode: Int = ls.login(
                    directNumber,
                    phoneNumber,
                    password,
                    applicationContext,
                    getSharedPreferences("userData", Context.MODE_PRIVATE)
                )

                if (responseCode == LoginResponseCode.AUTH_SUCCESS) {
                    this.redirectToTicketListActivity()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        /** Show or hide password on click listeners */
        showPasswordBtn.setOnClickListener() {
            passwordInput.transformationMethod = HideReturnsTransformationMethod.getInstance()
            showPasswordBtn.visibility = View.GONE;
            hidePasswordBtn.visibility = View.VISIBLE;
        }

        hidePasswordBtn.setOnClickListener() {
            passwordInput.transformationMethod = PasswordTransformationMethod.getInstance()
            showPasswordBtn.visibility = View.VISIBLE;
            hidePasswordBtn.visibility = View.GONE;
        }

        val adapter = ArrayAdapter.createFromResource(
            applicationContext,
            spinnerItems,
            direct_number_spinner
        )
        adapter.setDropDownViewResource(R.layout.direct_number_dropdown_spinner)
        directNumberInput.adapter = adapter

    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)
        val isLoggedIn: Boolean = sharedPreferences.getBoolean("IS_AUTHENTICATED", false)
        if (isLoggedIn) {
            this.redirectToTicketListActivity()
        }
    }
}
