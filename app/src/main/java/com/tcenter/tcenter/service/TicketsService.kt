package com.tcenter.tcenter.service

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


//}http://www.tcenter.pl/api/v/mobile/get-tickets

class TicketsService {

    fun getTicketsByUserIdAndTicketStatus(userId: Int, status: Int, ticketListLayout: LinearLayout, context: Context)
    {
        /** PARSE JSON */
        var jsonResponse: JSONObject = JSONObject("{}")
        val rs: RequestService = RequestService()
        val response = rs.getTicketsRequest(userId, status)
        try {
            jsonResponse = JSONObject("{\"json\": $response}")
            println(jsonResponse)
        }  catch (e: JSONException) {
            Log.e("JSONE", e.toString());
        }

        val json = JSONArray(jsonResponse.getString("json"))

        /** REMOVE ALL CHILD VIEWS OF TICKET LIST LAYOUT */
        ticketListLayout.removeAllViews()
        for(i in 0 until json.length())
        {
            /** GENERATING CLICKABLE TICKETS PREVIEWS */
            val ticket: JSONObject = json.getJSONObject(i)
            println(ticket)

            val id: Int                  = ticket.getInt("id")
            val topic: String            = ticket.getString("topic")
            val content: String          = ticket.getString("content")
            val deadlineTime: JSONObject = ticket.getJSONObject("deadlineTime")
            val isUrgent: Boolean        = ticket.getBoolean("urgentStatus")


            val ticketViewLayout = LinearLayout(context)
            ticketViewLayout.setOrientation(LinearLayout.VERTICAL);
            ticketViewLayout.setPadding(100, 100, 100, 100)

            /** TICKET PREVIEW CONFIG */

            val topicTextView: TextView = TextView(context)
            topicTextView.setText(topic)
            topicTextView.textSize = 30.0F
            topicTextView.setTextColor(Color.BLACK)
            topicTextView.setTypeface(null, Typeface.BOLD);

            val contentTextView: TextView = TextView(context)
            contentTextView.setText(content)
            contentTextView.textSize = 24.0F

            val deadlineTextView: TextView = TextView(context)
            deadlineTextView.setText("URGENT Deadline: 12 JAN 9:30")
            deadlineTextView.textSize = 24.0F

            ticketViewLayout.addView(topicTextView)
            ticketViewLayout.addView(contentTextView)
            ticketViewLayout.addView(deadlineTextView)

            ticketListLayout.addView(ticketViewLayout)

            ticketViewLayout.setOnClickListener()
            {
                println(id)
            }

        }

    }

}