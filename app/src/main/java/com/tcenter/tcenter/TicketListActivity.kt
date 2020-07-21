package com.tcenter.tcenter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.tcenter.tcenter.helper.TicketStatus
import com.tcenter.tcenter.service.TicketsService


class TicketListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ticket_list)

        /** GETTING USER ID TO LOAD HIS TICKETS */
        val sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)
        val userId: Int = sharedPreferences.getInt("ID", 0)

        /** LAYOUT ELEMENTS */
        val ticketListScrollView: ScrollView = findViewById(R.id.ticketsScroll)
        val ticketListLayout: LinearLayout = findViewById(R.id.ticketList)
        val toDoBtn: ImageButton = findViewById(R.id.toDoBtn)
        val solvedBtn: ImageButton           = findViewById(R.id.solvedBtn)
        val sentByMeBtn: ImageButton         = findViewById(R.id.sentByMeBtn)
        val sentDoneBtn: ImageButton         = findViewById(R.id.sentDoneBtn)
        val ticketsTypeHeader: TextView = findViewById(R.id.ticketsTypeHeader)

        ticketListLayout.removeAllViews()

        val ts = TicketsService()
        ts.getTicketsByUserIdAndTicketStatus(userId, TicketStatus.TO_DO, ticketListLayout, applicationContext, ticketListScrollView, 0)

        /** LOAD TO_DO TICKETS LIST */
        toDoBtn.setOnClickListener() {
            ticketsTypeHeader.text = getString(R.string.toDo)
            ts.getTicketsByUserIdAndTicketStatus(userId, TicketStatus.TO_DO, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }
        /** LOAD SOLVED TICKETS LIST */
        solvedBtn.setOnClickListener() {
            ticketsTypeHeader.text = getString(R.string.solved)
            ts.getTicketsByUserIdAndTicketStatus(userId, TicketStatus.SOLVED, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }
        /** LOAD SENT BY ME TICKETS LIST */
        sentByMeBtn.setOnClickListener() {
            ticketsTypeHeader.text = getString(R.string.sentByMe)
            ts.getTicketsByUserIdAndTicketStatus(userId, TicketStatus.SENT_BY_ME, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }
        /** LOAD SENT DONE TICKETS LIST */
        sentDoneBtn.setOnClickListener() {
            ticketsTypeHeader.text = getString(R.string.sentDone)
            ts.getTicketsByUserIdAndTicketStatus(userId, TicketStatus.SENT_DONE, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

        /** REDIRECT TO CREATE TICKET ACTIVITY */
        val createTicketBtn: Button = findViewById(R.id.createTicketBtn)
        createTicketBtn.setOnClickListener() {
            val intent = Intent(this, CreateTicketActivity::class.java)
            startActivity(intent)
        }
    }
//LOGOUT PROCEDURE
//    println("CLICK LOGOUT")
//            val editor = sharedPreferences.edit()
//            editor.putBoolean("IS_AUTHENTICATED", false)
//            editor.apply()
}
