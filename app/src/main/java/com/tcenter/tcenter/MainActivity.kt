package com.tcenter.tcenter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private fun redirectToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun redicrectToTicketListActivity()
    {
        val intent = Intent(this, TicketListActivity::class.java)
        startActivity(intent)
    }

    /**
     * Checks if user is logged in and redirect him to:
     * Login activity if he's not
     * Or
     * Ticket list view if he is
     */
    private fun checkIfUserIsAuthenticated(isAuthenticated: Boolean, sharedPreferences: SharedPreferences)
    {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)
        val isAuthenticated: Boolean = sharedPreferences.contains("IS_AUTHENTICATED")

        this.checkIfUserIsAuthenticated(isAuthenticated, sharedPreferences)
    }
}
