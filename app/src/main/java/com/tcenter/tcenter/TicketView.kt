package com.tcenter.tcenter

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.tcenter.tcenter.service.RequestService
import com.tcenter.tcenter.service.TicketsService

class TicketView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ticket_view)

        /** get ticket id and user id parameters to load data for it */
        val b: Bundle = intent.extras!!

        val ticketId: Int = b.getInt("id")
        val userId: Int   = b.getInt("userId")

        /** Load ticket data and show it on screen */
        val ts: TicketsService = TicketsService()
        ts.makeTicketView(ticketId, userId, this)

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

}