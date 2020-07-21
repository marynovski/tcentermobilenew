package com.tcenter.tcenter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.tcenter.tcenter.service.ChatService
import com.tcenter.tcenter.service.RequestService
import com.tcenter.tcenter.service.TicketsService
import kotlinx.android.synthetic.main.ticket_view.*

class TicketView : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ticket_view)



        /** get ticket id and user id parameters to load data for it */
        val b: Bundle = intent.extras!!

        val ticketId: Int = b.getInt("id")
        val userId: Int   = b.getInt("userId")
        val ticketStatus: Int = b.getInt("ticketStatus")
        this.showOrHideReopenTicketBtn(ticketStatus)

        /** Load ticket data and show it on screen */
        val ts: TicketsService = TicketsService()
        ts.makeTicketView(ticketId, userId, this, ticketStatus)

        /** BACK BUTTON SET ON CLICK LSITENER */
        val backBtn: ImageView = findViewById(R.id.back_button)
        backBtn.setOnClickListener() {
            this.redirectToTicketListActivity()
        }

        /** SEND TEXT MESSAGE */
        val cs = ChatService()
        val rs = RequestService()
        val messageInput: EditText = findViewById(R.id.message_input)
        val sendMsgBtn: ImageView = findViewById(R.id.send_message_button)
        val reopenTicketCheckBox: CheckBox = findViewById(R.id.reopen_ticket_checkbox)
        val closeTicketCheckbox: CheckBox  = findViewById(R.id.close_ticket_checkbox)
        sendMsgBtn.setOnClickListener {
            val message: String = messageInput.text.toString()
            val closingTicket: Boolean = closeTicketCheckbox.isChecked
            val reopeningTicket: Boolean = reopenTicketCheckBox.isChecked
            /** CLEAR MSG INPUT */
            messageInput.text.clear()
            if (closingTicket) {
                if (message == "") {
                    Toast.makeText(applicationContext, "Message cannot be empty", Toast.LENGTH_SHORT).show()
                } else {
                    rs.closeTicketRequest(ticketId, userId, message)
                    reopenTicketCheckBox.visibility = View.VISIBLE
                    closeTicketCheckbox.visibility  = View.GONE
                    closeTicketCheckbox.isChecked = false
                }
            } else if(reopeningTicket) {
                if (message == "") {
                    Toast.makeText(applicationContext, "Message cannot be empty", Toast.LENGTH_SHORT).show()
                } else {
                    rs.reopenTicketRequest(ticketId, userId, message)
                    reopenTicketCheckBox.visibility = View.GONE
                    closeTicketCheckbox.visibility  = View.VISIBLE
                    reopenTicketCheckBox.isChecked = false
                }
            }
            else {
                cs.sendTextMessage(ticketId, 150, userId, message, this)
            }
        }
    }

    /**
     * After click back button - go to ticket list activity (default - To do tickets list)
     */
    private fun redirectToTicketListActivity()
    {
        val intent = Intent(this, TicketListActivity::class.java)
        startActivity(intent)
    }

    /**
     * If ticket is closed - reopen ticket button should be gone
     */
    private fun showOrHideReopenTicketBtn(status: Int)
    {
        val reopenTicketCheckBox: CheckBox = findViewById(R.id.reopen_ticket_checkbox)
        val closeTicketCheckbox: CheckBox  = findViewById(R.id.close_ticket_checkbox)

        if (status == 1 || status == 3) {
            reopenTicketCheckBox.visibility = View.GONE
            closeTicketCheckbox.visibility  = View.VISIBLE
        } else {
            reopenTicketCheckBox.visibility = View.VISIBLE
            closeTicketCheckbox.visibility  = View.GONE
        }
    }




}