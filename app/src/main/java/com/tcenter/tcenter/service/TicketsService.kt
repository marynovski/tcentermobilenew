package com.tcenter.tcenter.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.tcenter.tcenter.R
import com.tcenter.tcenter.TicketView
import kotlinx.android.synthetic.main.activity_ticket_view.view.*
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Thread.sleep


//}http://www.tcenter.pl/api/v/mobile/get-tickets

class TicketsService {

    fun getTicketsByUserIdAndTicketStatus(userId: Int, status: Int, ticketListLayout: LinearLayout, context: Context, scrollView: ScrollView, loadedTicketsCount: Int) {
        /** PARSE JSON */
        var jsonResponse: JSONObject = JSONObject("{}")
        val rs: RequestService = RequestService()
        val response = rs.getTicketsRequest(userId, status, loadedTicketsCount-2, loadedTicketsCount+10)
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
                val intent = Intent(context, TicketView::class.java)
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
                val b: Bundle = Bundle()
                b.putInt("id", id)
                b.putInt("userId", userId)
                intent.putExtras(b)
                startActivity(context, intent, b)
            }

        }

        val loadMoreTicketsBtn: Button = Button(context)
        loadMoreTicketsBtn.setText("Load more tickets...")
        ticketListLayout.addView(loadMoreTicketsBtn)

        loadMoreTicketsBtn.setOnClickListener()
        {
            this.getTicketsByUserIdAndTicketStatus(userId, status, ticketListLayout, context, scrollView, loadedTicketsCount+10)
        }

    }

    fun getTicketByIdAndUserId(id: Int, userId: Int): JSONObject {
        var jsonResponse: JSONObject = JSONObject("{}")
        val rs: RequestService = RequestService()
        val response = rs.getTicketRequest(id, userId)
        try {
            jsonResponse = JSONObject("{\"json\": $response}")
            println(jsonResponse)
        }  catch (e: JSONException) {
            Log.e("JSONE", e.toString());
        }

        return jsonResponse
    }

    fun makeTicketView(ticketId: Int, userId: Int, activity: Activity)
    {

        val backTextView: TextView = activity.findViewById(R.id.backHeaderText)
        backTextView.setText("Back")



        val jsonResponse: JSONObject = this.getTicketByIdAndUserId(ticketId, userId)
        val json = JSONArray(jsonResponse.getString("json"))
        println(json)

        /** TICKET DATA FROM JSON */
        val jsonToParse: JSONObject = json.getJSONObject(0)
        val topic: String = jsonToParse.getString("topic")
        val content: String = jsonToParse.getString("content")
        val author: String = "Author: "+jsonToParse.getString("senderName")
        val companyIsNull: Boolean = jsonToParse.isNull("company")
        var company: String = "Company: N/A"

        if (!companyIsNull) {
            company = "Company: "+jsonToParse.getString("company")
        }

        val project: String = "Project: "+jsonToParse.getString("project")

        /** SET TOPIC TEXT */
        val ticketTopicTextView: TextView = activity.findViewById(R.id.ticketTopicText)
        ticketTopicTextView.setText(topic)

        /** SET CONTENT TEXT */
        val ticketContentText: TextView = activity.findViewById(R.id.ticketContentText)
        ticketContentText.setText(content)

        /** SET AUTHOR TEXT */
        val ticketAuthorText: TextView = activity.findViewById(R.id.authorText)
        ticketAuthorText.setText(author)

        /** SET COMPANY TEXT */
        val companyText: TextView = activity.findViewById(R.id.companyText)
        companyText.setText(company)

        /** SET PROJECT TEXT */
        val projectText: TextView = activity.findViewById(R.id.projectName)
        projectText.setText(project)

        val ds = DateService()
        /** PARSE AND SET DEADLINE DATETIME */
        val deadlineDateTime: String = "Deadline: "+ds.parseDateTime(jsonToParse.getJSONObject("deadlineTime"))
        val deadlineTextView: TextView = activity.findViewById(R.id.deadlineText)
        deadlineTextView.setText(deadlineDateTime)
        /** PARSE AND SET ADDED/CREATED DATETIME */
        val createdDateTime: String = "Added: "+ds.parseDateTime(jsonToParse.getJSONObject("createdTime"))
        val addedTextView: TextView = activity.findViewById(R.id.addedText)
        addedTextView.setText(createdDateTime)
    }
}