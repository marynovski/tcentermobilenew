package com.tcenter.tcenter.service

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.startActivity
import com.tcenter.tcenter.R
import com.tcenter.tcenter.TicketView
import com.tcenter.tcenter.helper.TicketStatus
import kotlinx.android.synthetic.main.activity_ticket_view.view.*
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Thread.sleep


//}http://www.tcenter.pl/api/v/mobile/get-tickets

class TicketsService {

    private val STORAGE_PERMISSION_CODE: Int = 1000
    private val TO_DO: Int      = 1
    private val SOLVED: Int     = 2
    private val SENT_BY_ME: Int = 3
    private val SENT_DONE: Int  = 4

    fun getTicketsByUserIdAndTicketStatus(userId: Int, status: Int, ticketListLayout: LinearLayout, context: Context, scrollView: ScrollView, loadedTicketsCount: Int) {
        /** PARSE JSON */
        var jsonResponse: JSONObject = JSONObject("{}")
        val rs: RequestService = RequestService()
        val response = rs.getTicketsRequest(userId, status, loadedTicketsCount-2, loadedTicketsCount+10)
        println(response)

        var isResponseNull: Boolean

        if (response == "{}" ) {
            isResponseNull = true
        } else {
            isResponseNull = false
        }

        try {
            jsonResponse = JSONObject("{\"json\": $response}")
        }  catch (e: JSONException) {
            Log.e("JSONE", e.toString());
        }


        /** REMOVE ALL CHILD VIEWS OF TICKET LIST LAYOUT */
        ticketListLayout.removeAllViews()

        if (!isResponseNull) {
            val json = JSONArray(jsonResponse.getString("json"))
            for (i in 0 until json.length()) {
                /** GENERATING CLICKABLE TICKETS PREVIEWS */
                val ticket: JSONObject = json.getJSONObject(i)
                println(ticket)

                val id: Int = ticket.getInt("id")
                val topic: String = ticket.getString("topic")
                var content: String = ticket.getString("content")

                if (content.length > 100) {
                    content = content.substring(0, 100)+"..."
                }

                val isUrgent: Boolean = ticket.getBoolean("urgentStatus")
                val ds = DateService()
                var deadlineDateTime: String = ""

                if (isUrgent) {
                    deadlineDateTime = "URGENT            Deadline: " + ds.parseDateTime(ticket.getJSONObject("deadlineTime"))
                } else {
                    deadlineDateTime = "Deadline: " + ds.parseDateTime(ticket.getJSONObject("deadlineTime"))
                }

                val ticketViewLayout = LinearLayout(context)
                ticketViewLayout.setOrientation(LinearLayout.VERTICAL);
                ticketViewLayout.setPadding(100, 20, 100, 20)

                /** TICKET PREVIEW CONFIG */

                val topicTextView: TextView = TextView(context)
                topicTextView.setText(topic)
                topicTextView.textSize = 24.0F
                topicTextView.setTextColor(Color.BLACK)
                topicTextView.setTypeface(null, Typeface.BOLD);

                val contentTextView: TextView = TextView(context)
                contentTextView.setText(content)
                contentTextView.textSize = 16.0F

                val deadlineTextView: TextView = TextView(context)
                deadlineTextView.setText(deadlineDateTime)
                deadlineTextView.textSize = 16.0F
                deadlineTextView.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                deadlineTextView.typeface = Typeface.DEFAULT_BOLD

                if (ds.checkIfDeadlineIsOver(ticket.getJSONObject("deadlineTime")) && status == 1 || status == 3) {
                    deadlineTextView.setTextColor(Color.parseColor("#D12043"))
                }

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
                    b.putInt("ticketStatus", status)
                    intent.putExtras(b)
                    startActivity(context, intent, b)
                }

            }

            if (json.length() < 10) {
                Toast.makeText(context, "There are no more tickets", Toast.LENGTH_SHORT).show()
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

    fun makeTicketView(ticketId: Int, userId: Int, activity: Activity, ticketStatus: Int)
    {

        val backTextView: TextView = activity.findViewById(R.id.backHeaderText)
        backTextView.setText("Back")



        val jsonResponse: JSONObject = this.getTicketByIdAndUserId(ticketId, userId)
        val json = JSONArray(jsonResponse.getString("json"))
        println(json)

        /** TICKET DATA FROM JSON */
        val jsonToParse: JSONObject = json.getJSONObject(0)
        val id: String = jsonToParse.getString("id")
        val topic: String = jsonToParse.getString("topic")
        val content: String = jsonToParse.getString("content")
        val author: String = "Author: "+jsonToParse.getString("senderName")
        val companyIsNull: Boolean = jsonToParse.isNull("company")
        var company: String = "Company: N/A"
        val status: Int = jsonToParse.getInt("status")

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

        /** ATTACHMENTS TO DOWNLOAD */
        val attachments: JSONArray = jsonToParse.getJSONArray("attachments")
        for (i in 0 until attachments.length())
        {
            val fileName: String = attachments[i] as String
            val attachmentsScroll: LinearLayout = activity.findViewById(R.id.attachementsList)
            val attachmentImageButton: ImageButton = ImageButton(activity.applicationContext)
            attachmentImageButton.setImageResource(R.drawable.ic_launcher_foreground)
            attachmentsScroll.addView(attachmentImageButton)

            val rs = RequestService()
            attachmentImageButton.setOnClickListener()
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(activity.applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED) {

                        requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
                    } else {
                        rs.downloadAttachementRequest(fileName, activity.applicationContext)
                    }
                } else {
                    rs.downloadAttachementRequest(fileName, activity.applicationContext)
                }

                println("DOWNLOAD $fileName")
            }
        }

            val closeTicketButton: Button = activity.findViewById(R.id.closeTicketBtn)
            closeTicketButton.setOnClickListener() {
                this.closeTicket(id.toInt(), userId, activity)
            }

            val reopenTicketButton: Button = activity.findViewById(R.id.reopenTicketButton)
            reopenTicketButton.setOnClickListener() {
                this.reopenTicket(id.toInt(), userId, activity)
            }
    }

    private fun closeTicket(id: Int, userId: Int, activity: Activity)
    {
        val rs = RequestService()
        if(rs.closeTicketRequest(id, userId) == "true") {
            Toast.makeText(activity.applicationContext , "Ticket has been closed", Toast.LENGTH_SHORT).show()

            val closeTicketBtn: Button = activity.findViewById(R.id.closeTicketBtn)
            val reopenTicketBtn: Button = activity.findViewById(R.id.reopenTicketButton)

            closeTicketBtn.visibility = View.INVISIBLE
            reopenTicketBtn.visibility = View.VISIBLE
        } else {
            Toast.makeText(activity.applicationContext, "Ticket hasn't been closed", Toast.LENGTH_SHORT).show()
        }


    }

    private fun reopenTicket(id: Int, userId: Int, activity: Activity)
    {
        val rs = RequestService()
        val reopen_ticket_json_response: JSONObject = JSONObject(rs.reopenTicketRequest(id, userId))

        val responseMessage: String = reopen_ticket_json_response.getString("msg")
        val result: Boolean = reopen_ticket_json_response.getBoolean("result")

        if (result) {
            Toast.makeText(activity.applicationContext , responseMessage, Toast.LENGTH_SHORT).show()

            val closeTicketBtn: Button  = activity.findViewById(R.id.closeTicketBtn)
            val reopenTicketBtn: Button = activity.findViewById(R.id.reopenTicketButton)

            closeTicketBtn.visibility = View.VISIBLE
            reopenTicketBtn.visibility = View.INVISIBLE
        } else {
            Toast.makeText(activity.applicationContext, responseMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun createTicket()
    {
        val projectName = "Android"
        val topic       = "Android Create Ticket Test"
        val addedUserId = 150
        val receivedUserId = 150
        val deadLine = "2020-07-08 11:40:59"
        val urgentStatus = true
        val content = "Test ticket created via android tceneter app"

        val rs = RequestService()
        val createTicketJsonResponse: JSONObject = JSONObject(rs.createTicketRequest(projectName, topic, addedUserId, receivedUserId, deadLine, urgentStatus, content))

        println(createTicketJsonResponse)

    }
}