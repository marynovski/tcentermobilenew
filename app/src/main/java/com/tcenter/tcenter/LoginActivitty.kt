package com.tcenter.tcenter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.tcenter.tcenter.service.Login

class LoginActivitty : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /** LOGIN LAYOUT ELEMENTS */
        val directNumberInput: Spinner    = findViewById(R.id.directNumberSelect)
        val phoneNumberInput: EditText    = findViewById(R.id.phoneInput)
        val passwordInput: EditText       = findViewById(R.id.passwordInput)
        val loginBtn: Button              = findViewById(R.id.signInBtn)
        val showHidePasswordBtn: TextView = findViewById(R.id.showHidePass)
        val ticketListIntent = Intent(this, TicketListActivity::class.java)

        /**
         * MAKE LOGIN REQUEST ON SIGN IN BUTTON CLICK
         * CHECK CREDENTIALS
         * IF LOGGED SUCCESSFULLY -> CREATE/UPDATE SHARED PREFERENCES FILE
         * IF NOT -> SHOW NOTIFICATION
         */
        loginBtn.setOnClickListener() {
            println("CLICKED")
            val phoneNumber: String = directNumberInput.selectedItem.toString()+phoneNumberInput.text.toString()
            val password: String    = passwordInput.text.toString()
            /** LOGIN SERVICE */
            val ls: Login = Login()
            println("LEST MAKE LS>LOGIN")
            val responseCode: Int = ls.login(phoneNumber, password, applicationContext, getSharedPreferences("userData", Context.MODE_PRIVATE))

            if (responseCode == 1) {
                this.redicrectToTicketListActivity()
            }
        }

        /**
         * SHOW AND HIDE PASSWORD ON CLICK
         */
        showHidePasswordBtn.setOnClickListener() {
            if(showHidePasswordBtn.text.equals("SHOW")) {
                passwordInput.transformationMethod = HideReturnsTransformationMethod.getInstance()
                showHidePasswordBtn.text = "HIDE"
            } else {
                passwordInput.transformationMethod = PasswordTransformationMethod.getInstance()
                showHidePasswordBtn.text = "SHOW"
            }
        }
    }

    private fun redicrectToTicketListActivity()
    {
        val intent = Intent(this, TicketListActivity::class.java)
        startActivity(intent)
    }
}
