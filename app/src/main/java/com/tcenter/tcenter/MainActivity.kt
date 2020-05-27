package com.tcenter.tcenter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * 1.    Check if shared preferences file "UserData.xml" exists.
         * --------------------------------------------------------------
         * 1.1   If not   -> redirect to login activity
         * 1.2   If yes   -> check if Boolean IS_AUTHENCTICATED == true
         * --------------------------------------------------------------
         * 1.2.1 If true  -> redirect to ticket list activity
         * 1.2.2 If false -> redirect to login activity
         */
        val sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)
        val isAuthenticated: Boolean = sharedPreferences.contains("IS_AUTHENTICATED")
        if (isAuthenticated) {
            val isLoggedIn: Boolean = sharedPreferences.getBoolean("IS_AUTHENTICATED", false)
            if (isLoggedIn) {
                this.redicrectToTicketListActivity()
            } else {
                this.redirectToLoginActivity()
            }
        } else {
            this.redirectToLoginActivity()
        }


    }

    /** REDIRECT TO LOGIN ACTIVITY */
    private fun redirectToLoginActivity() {
        val intent = Intent(this, LoginActivitty::class.java)
        startActivity(intent)
    }

    private fun redicrectToTicketListActivity()
    {
        val intent = Intent(this, TicketListActivity::class.java)
        startActivity(intent)
    }
}
