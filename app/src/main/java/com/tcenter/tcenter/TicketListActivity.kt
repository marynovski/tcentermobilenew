package com.tcenter.tcenter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.tcenter.tcenter.helper.TicketStatus
import com.tcenter.tcenter.service.TicketsService


class TicketListActivity : AppCompatActivity() {

    @ExperimentalStdlibApi
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
        val logoutBtn: ImageView = findViewById(R.id.logout_btn)

        ticketListLayout.removeAllViews()

        val ts = TicketsService()
        ts.getTicketsByUserIdAndTicketStatus(userId, TicketStatus.TO_DO, ticketListLayout, applicationContext, ticketListScrollView, 0)

        /** LOAD TO_DO TICKETS LIST */
        toDoBtn.setOnClickListener {
            ticketsTypeHeader.text = getString(R.string.tlist_ticket_type_header_to_do)
            setImagesOfTicketTypesButtons(TicketStatus.TO_DO, toDoBtn)
            ts.getTicketsByUserIdAndTicketStatus(userId, TicketStatus.TO_DO, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }
        /** LOAD SOLVED TICKETS LIST */
        solvedBtn.setOnClickListener {
            ticketsTypeHeader.text = getString(R.string.tlist_ticket_type_header_solved)
            setImagesOfTicketTypesButtons(TicketStatus.SOLVED, solvedBtn)
            ts.getTicketsByUserIdAndTicketStatus(userId, TicketStatus.SOLVED, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }
        /** LOAD SENT BY ME TICKETS LIST */
        sentByMeBtn.setOnClickListener() {
            ticketsTypeHeader.text = getString(R.string.tlist_ticket_type_header_sent_by_me)
            setImagesOfTicketTypesButtons(TicketStatus.SENT_BY_ME, sentByMeBtn)
            ts.getTicketsByUserIdAndTicketStatus(userId, TicketStatus.SENT_BY_ME, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }
        /** LOAD SENT DONE TICKETS LIST */
        sentDoneBtn.setOnClickListener {
            ticketsTypeHeader.text = getString(R.string.tlist_ticket_type_header_sent_done)
            setImagesOfTicketTypesButtons(TicketStatus.SENT_DONE, sentDoneBtn)
            ts.getTicketsByUserIdAndTicketStatus(userId, TicketStatus.SENT_DONE, ticketListLayout, applicationContext, ticketListScrollView, 0)
        }

        /** REDIRECT TO CREATE TICKET ACTIVITY */
        val createTicketBtn: ImageView = findViewById(R.id.create_ticket_button)
        createTicketBtn.setOnClickListener() {
            val intent = Intent(this, CreateTicketActivity::class.java)
            startActivity(intent)
        }

        logoutBtn.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putBoolean("IS_AUTHENTICATED", false)
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setImagesOfTicketTypesButtons(ticketType: Int, button: ImageButton)
    {
        val toDoBtn: ImageButton = findViewById(R.id.toDoBtn)
        val solvedBtn: ImageButton = findViewById(R.id.solvedBtn)
        val sendByMeBtn: ImageButton = findViewById(R.id.sentByMeBtn)
        val sentDoneBtn: ImageButton = findViewById(R.id.sentDoneBtn)

        toDoBtn.setImageResource(R.drawable.todoiconnoactive)
        solvedBtn.setImageResource(R.drawable.solvedicon)
        sendByMeBtn.setImageResource(R.drawable.sentbyme)
        sentDoneBtn.setImageResource(R.drawable.sentdoneicon)

        val toDoText: TextView = findViewById(R.id.to_do_text)
        val solvedText: TextView = findViewById(R.id.solved_text)
        val sendByMeText: TextView = findViewById(R.id.sent_by_me_text)
        val sentDoneText: TextView = findViewById(R.id.sent_done_text)

        toDoText.setTextColor(Color.parseColor("#818181"))
        solvedText.setTextColor(Color.parseColor("#818181"))
        sendByMeText.setTextColor(Color.parseColor("#818181"))
        sentDoneText.setTextColor(Color.parseColor("#818181"))

        when (ticketType) {
            TicketStatus.TO_DO -> {
                button.setImageResource(R.drawable.todoicon)
                toDoText.setTextColor(Color.parseColor("#000000"))
            }
            TicketStatus.SOLVED -> {
                button.setImageResource(R.drawable.solvediconactive)
                solvedText.setTextColor(Color.parseColor("#000000"))
            }
            TicketStatus.SENT_BY_ME -> {
                button.setImageResource(R.drawable.sentbymeactive)
                sendByMeText.setTextColor(Color.parseColor("#000000"))
            }
            TicketStatus.SENT_DONE -> {
                button.setImageResource(R.drawable.sentdoneactive)
                sentDoneText.setTextColor(Color.parseColor("#000000"))
            }
        }
    }
}
