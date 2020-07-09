package com.tcenter.tcenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView
import com.tcenter.tcenter.service.TicketsService

class CreateTicketActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_ticket_view)

        val submitBtn: Button = findViewById(R.id.submitBtn)
        val ts = TicketsService()
        submitBtn.setOnClickListener {
            ts.createTicket()
        }

        val contentScroll: ScrollView = findViewById(R.id.contentScroll)
    }
}