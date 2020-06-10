package com.tcenter.tcenter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.Window
import android.widget.*
import com.tcenter.tcenter.service.Login

class LoginActivitty : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        /** LOGIN LAYOUT ELEMENTS */
        val directNumberInput: Spinner    = findViewById(R.id.directNumberSelect)
        val phoneNumberInput: EditText    = findViewById(R.id.phoneInput)
        val passwordInput: EditText       = findViewById(R.id.passwordInput)
        val loginBtn: Button              = findViewById(R.id.signInBtn)
        val showPasswordBtn: ImageButton = findViewById(R.id.showPasswordBtn)
        val hidePasswordBtn: ImageButton = findViewById(R.id.hidePassBtn)
        val ticketListIntent = Intent(this, TicketListActivity::class.java)

        /**
         * MAKE LOGIN REQUEST ON SIGN IN BUTTON CLICK
         * CHECK CREDENTIALS
         * IF LOGGED SUCCESSFULLY -> CREATE/UPDATE SHARED PREFERENCES FILE
         * IF NOT -> SHOW NOTIFICATION
         */
        loginBtn.setOnClickListener() {
            println("CLICKED")
            val directNumber: String = directNumberInput.selectedItem.toString()
            val phoneNumber: String = phoneNumberInput.text.toString()
            val password: String    = passwordInput.text.toString()
            /** LOGIN SERVICE */
            val ls: Login = Login()
            println("LEST MAKE LS>LOGIN")
            val responseCode: Int = ls.login(directNumber, phoneNumber, password, applicationContext, getSharedPreferences("userData", Context.MODE_PRIVATE))

            if (responseCode == 1) {
                this.redicrectToTicketListActivity()
            }
        }

        /**
         * SHOW AND HIDE PASSWORD ON CLICK
         */
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
    }

    private fun redicrectToTicketListActivity()
    {
        val intent = Intent(this, TicketListActivity::class.java)
        startActivity(intent)
    }
}
