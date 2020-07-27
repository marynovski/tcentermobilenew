package com.tcenter.tcenter.service

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.tcenter.tcenter.R
import kotlinx.android.synthetic.main.ticket_view.view.*

class ChatService {

    fun sendTextMessage(ticketId: Int, receiverId: Int, recipientId: Int, message: String, activity: Activity)
    {
        val rs = RequestService()
        if (rs.sendTextMessageRequest(ticketId, receiverId, recipientId, message) == "OK") {
            Toast.makeText(activity.applicationContext, "Message has been sent", Toast.LENGTH_SHORT).show()
            val messageBox = activity.findViewById<LinearLayout>(R.id.messages_box_layout)
            val messageTextView = TextView(activity.applicationContext)
            messageTextView.text = message
            messageTextView.setBackgroundColor(Color.parseColor("#333333"))
            messageTextView.setTextColor(Color.parseColor("#FFFFFF"))
            messageBox.addView(messageTextView)
        } else {
            Toast.makeText(activity.applicationContext, "Message sending failed", Toast.LENGTH_SHORT).show()
        }
    }
}