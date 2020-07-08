package com.tcenter.tcenter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.tcenter.tcenter.helper.TicketStatus
import com.tcenter.tcenter.service.TicketsService


class TicketListActivity : AppCompatActivity() {

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


        ts.getTicketsByUserIdAndTicketStatus(userId, TicketStatus.TO_DO, ticketListLayout, applicationContext, ticketListScrollView, 0)


        toDoBtn.setOnClickListener()
        {

            ticketsTypeHeader.text = R.string.toDo.toString()
            ts.getTicketsByUserIdAndTicketStatus(userId, TicketStatus.TO_DO, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

        solvedBtn.setOnClickListener()
        {
            ticketsTypeHeader.text = R.string.solved.toString()
            ts.getTicketsByUserIdAndTicketStatus(userId, TicketStatus.SOLVED, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

        sentByMeBtn.setOnClickListener()
        {
            ticketsTypeHeader.text = R.string.sentByMe.toString()
            ts.getTicketsByUserIdAndTicketStatus(userId, TicketStatus.SENT_BY_ME, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

        sentDoneBtn.setOnClickListener()
        {
            ticketsTypeHeader.text = R.string.sentDone.toString()
            ts.getTicketsByUserIdAndTicketStatus(userId, TicketStatus.SENT_DONE, ticketListLayout, applicationContext, ticketListScrollView, 0)
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
}
