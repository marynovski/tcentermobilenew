package com.tcenter.tcenter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import com.tcenter.tcenter.service.RequestService
import com.tcenter.tcenter.service.TicketsService

class TicketListActivity : AppCompatActivity() {

    private val TO_DO      = 1
    private val SOLVED     = 2
    private val SENT_BY_ME = 3
    private val SENT_DONE  = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_list)

        val ticketListLayout: LinearLayout = findViewById(R.id.ticketList)
        val sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)
        val userId: Int = sharedPreferences.getInt("ID", 0)
        val ts: TicketsService = TicketsService()
        ts.getTicketsByUserIdAndTicketStatus(userId, this.TO_DO, ticketListLayout, applicationContext)

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

    override fun onResume() {
        super.onResume()

        val ticketListLayout: LinearLayout = findViewById(R.id.ticketList)
        val sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)
        val userId: Int = sharedPreferences.getInt("ID", 0)
        val ts: TicketsService = TicketsService()
        val toDoBtn: Button = findViewById(R.id.toDoBtn)
        val solvedBtn: Button = findViewById(R.id.solvedBtn)
        val sentByMeBtn: Button = findViewById(R.id.sentByMeBtn)
        val sentDoneBtn: Button = findViewById(R.id.sentDoneBtn)

        toDoBtn.setOnClickListener()
        {
            ts.getTicketsByUserIdAndTicketStatus(userId, this.TO_DO, ticketListLayout, applicationContext)
        }

        solvedBtn.setOnClickListener()
        {
            ts.getTicketsByUserIdAndTicketStatus(userId, this.SOLVED, ticketListLayout, applicationContext)
        }

        sentByMeBtn.setOnClickListener()
        {
            ts.getTicketsByUserIdAndTicketStatus(userId, this.SENT_BY_ME, ticketListLayout, applicationContext)
        }

        sentDoneBtn.setOnClickListener()
        {
            ts.getTicketsByUserIdAndTicketStatus(userId, this.SENT_DONE, ticketListLayout, applicationContext)
        }
    }
}
