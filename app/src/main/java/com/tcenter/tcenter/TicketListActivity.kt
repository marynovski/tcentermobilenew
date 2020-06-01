package com.tcenter.tcenter

import android.content.Context
import android.os.Bundle
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.tcenter.tcenter.service.TicketsService


class TicketListActivity : AppCompatActivity() {

    private val TO_DO      = 1
    private val SOLVED     = 2
    private val SENT_BY_ME = 3
    private val SENT_DONE  = 4

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

        ts.getTicketsByUserIdAndTicketStatus(userId, this.TO_DO, ticketListLayout, applicationContext, ticketListScrollView, 0)


        toDoBtn.setOnClickListener()
        {
            ts.getTicketsByUserIdAndTicketStatus(userId, this.TO_DO, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

        solvedBtn.setOnClickListener()
        {
            ts.getTicketsByUserIdAndTicketStatus(userId, this.SOLVED, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

        sentByMeBtn.setOnClickListener()
        {
            ts.getTicketsByUserIdAndTicketStatus(userId, this.SENT_BY_ME, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

        sentDoneBtn.setOnClickListener()
        {
            ts.getTicketsByUserIdAndTicketStatus(userId, this.SENT_DONE, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

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


    }
}
