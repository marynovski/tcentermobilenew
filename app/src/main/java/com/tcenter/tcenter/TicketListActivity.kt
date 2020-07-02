package com.tcenter.tcenter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.tcenter.tcenter.service.TicketsService


class TicketListActivity : AppCompatActivity() {

//    val TO_DO: int      = 1
//    val SOLVED     = 2
//    val SENT_BY_ME = 3
//    val SENT_DONE  = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ticket_list)

        val ticketListScrollView: ScrollView = findViewById(R.id.ticketsScroll)
        val ticketListLayout: LinearLayout = findViewById(R.id.ticketList)
        val sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)
        val userId: Int = sharedPreferences.getInt("ID", 0)
        val ts = TicketsService()
        val toDoBtn: ImageButton     = findViewById(R.id.toDoBtn)
        val solvedBtn: ImageButton   = findViewById(R.id.solvedBtn)
        val sentByMeBtn: ImageButton = findViewById(R.id.sentByMeBtn)
        val sentDoneBtn: ImageButton = findViewById(R.id.sentDoneBtn)
        val ticketsTypeHeader: TextView = findViewById(R.id.ticketsTypeHeader)

        ticketListLayout.removeAllViews()


        ts.getTicketsByUserIdAndTicketStatus(userId, 1, ticketListLayout, applicationContext, ticketListScrollView, 0)


        toDoBtn.setOnClickListener()
        {

            ticketsTypeHeader.text = "To do"
            ts.getTicketsByUserIdAndTicketStatus(userId, 1, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

        solvedBtn.setOnClickListener()
        {
            ticketsTypeHeader.text = "Solved"
            ts.getTicketsByUserIdAndTicketStatus(userId, 2, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

        sentByMeBtn.setOnClickListener()
        {
            ticketsTypeHeader.text = "Sent by me"
            ts.getTicketsByUserIdAndTicketStatus(userId, 3, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

        sentDoneBtn.setOnClickListener()
        {
            ticketsTypeHeader.text = "Sent done"
            ts.getTicketsByUserIdAndTicketStatus(userId, 4, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

        val logoutBtn: Button = findViewById(R.id.createTicketBtn)

        logoutBtn.setOnClickListener() {
            println("CLICK LOGOUT")
//            val editor = sharedPreferences.edit()
//            editor.putBoolean("IS_AUTHENTICATED", false)
//            editor.apply()

            val intent = Intent(this, CreateTicketActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()


    }
}
