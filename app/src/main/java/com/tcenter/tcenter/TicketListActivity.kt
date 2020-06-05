package com.tcenter.tcenter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.tcenter.tcenter.service.TicketsService


class TicketListActivity : AppCompatActivity() {

//    val TO_DO: int      = 1
//    val SOLVED     = 2
//    val SENT_BY_ME = 3
//    val SENT_DONE  = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_list)

        val ticketListScrollView: ScrollView = findViewById(R.id.ticketsScroll)
        val ticketListLayout: LinearLayout = findViewById(R.id.ticketList)
        val sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)
        val userId: Int = sharedPreferences.getInt("ID", 0)
        val ts: TicketsService = TicketsService()
        val toDoBtn: ImageButton     = findViewById(R.id.toDoBtn)
        val solvedBtn: ImageButton   = findViewById(R.id.solvedBtn)
        val sentByMeBtn: ImageButton = findViewById(R.id.sentByMeBtn)
        val sentDoneBtn: ImageButton = findViewById(R.id.sentDoneBtn)

        ticketListLayout.removeAllViews()


        ts.getTicketsByUserIdAndTicketStatus(userId, 1, ticketListLayout, applicationContext, ticketListScrollView, 0)


        toDoBtn.setOnClickListener()
        {
            ts.getTicketsByUserIdAndTicketStatus(userId, 1, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

        solvedBtn.setOnClickListener()
        {
            ts.getTicketsByUserIdAndTicketStatus(userId, 2, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

        sentByMeBtn.setOnClickListener()
        {
            ts.getTicketsByUserIdAndTicketStatus(userId, 3, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

        sentDoneBtn.setOnClickListener()
        {
            ts.getTicketsByUserIdAndTicketStatus(userId, 4, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

        val logoutBtn: Button = findViewById(R.id.createTicketBtn)

        logoutBtn.setOnClickListener() {
            println("CLICK LOGOUT")
            val editor = sharedPreferences.edit()
            editor.putBoolean("IS_AUTHENTICATED", false)
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()


    }
}
