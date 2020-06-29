package com.tcenter.tcenter

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.tcenter.tcenter.service.RequestService
import com.tcenter.tcenter.service.TicketsService
import kotlinx.android.synthetic.main.ticket_view.*

class TicketView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ticket_view)

        val reopenTicketBtn: Button = findViewById(R.id.reopenTicketButton)
        val closeTicketBtn: Button  = findViewById(R.id.closeTicketBtn)



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
        val backBtn: ImageButton = findViewById(R.id.backBtn)
        backBtn.setOnClickListener() {
            this.redirectToTicketListActivity()
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
        if (status == 1 || status == 3) {
            reopenTicketButton.visibility = View.GONE
            closeTicketBtn.visibility     = View.VISIBLE
        } else {
            reopenTicketButton.visibility = View.VISIBLE
            closeTicketBtn.visibility     = View.GONE
        }
    }

}