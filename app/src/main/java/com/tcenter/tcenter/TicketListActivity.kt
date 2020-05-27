package com.tcenter.tcenter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TicketListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_list)

//        val sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)
//        val logoutBtn: Button = findViewById(R.id.logoutBtn)
//
//        logoutBtn.setOnClickListener() {
//            println("CLICK LOGOUT")
//            val editor = sharedPreferences.edit()
//            editor.putBoolean("IS_AUTHENTICATED", false)
//            editor.apply()
//
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
    }
}
