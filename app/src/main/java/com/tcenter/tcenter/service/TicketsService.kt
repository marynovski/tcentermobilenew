package com.tcenter.tcenter.service

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.startActivity
import com.tcenter.tcenter.R
import com.tcenter.tcenter.TicketView
import com.tcenter.tcenter.entity.Ticket
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


//}http://www.tcenter.pl/api/v/mobile/get-tickets

class TicketsService {

    private val STORAGE_PERMISSION_CODE: Int = 1000

    fun getTicketsByUserIdAndTicketStatus(userId: Int, status: Int, ticketListLayout: LinearLayout, context: Context, scrollView: ScrollView, loadedTicketsCount: Int) {
        /** PARSE JSON */
        var jsonResponse: JSONObject = JSONObject("{}")
        val rs = RequestService()
        val response: String = rs.getTicketsRequest(userId, status, loadedTicketsCount-2, loadedTicketsCount+10)

        var isResponseNull: Boolean

        isResponseNull = response == "{}"

        try {
            jsonResponse = JSONObject("{\"json\": $response}")
        }  catch (e: JSONException) {
            Log.e("JSONE", e.toString())
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
                ticketViewLayout.orientation = LinearLayout.VERTICAL
                ticketViewLayout.setPadding(100, 20, 100, 20)
                ticketViewLayout.setBackgroundColor(Color.parseColor("#333333"))

                /** TICKET PREVIEW CONFIG */

                val topicTextView: TextView = TextView(context)
                topicTextView.text = topic
                topicTextView.textSize = 24.0F
                topicTextView.setTextColor(Color.WHITE)

                val contentTextView: TextView = TextView(context)
                contentTextView.text = content
                contentTextView.setTextColor(Color.WHITE)
                contentTextView.textSize = 16.0F

                val deadlineTextView: TextView = TextView(context)
                deadlineTextView.text = deadlineDateTime
                deadlineTextView.textSize = 16.0F
                deadlineTextView.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                deadlineTextView.typeface = Typeface.DEFAULT_BOLD

                if (ds.checkIfDeadlineIsOver(ticket.getJSONObject("deadlineTime")) && status == 1 || status == 3) {
                    deadlineTextView.setTextColor(Color.parseColor("#e83e0f"))
                } else {
                    deadlineTextView.setTextColor(Color.WHITE)
                }

                ticketViewLayout.addView(topicTextView)
                ticketViewLayout.addView(contentTextView)
                ticketViewLayout.addView(deadlineTextView)

                ticketListLayout.addView(ticketViewLayout)

                val param = ticketViewLayout.layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(0,0,0,2)
                ticketViewLayout.layoutParams = param

                ticketViewLayout.setOnClickListener()
                {
                    val intent = Intent(context, TicketView::class.java)
                    intent.flags = FLAG_ACTIVITY_NEW_TASK
                    val b: Bundle = Bundle()
                    b.putInt("id", id)
                    b.putInt("userId", userId)
                    b.putInt("ticketStatus", status)
                    intent.putExtras(b)
                    startActivity(context, intent, b)
                }

            }

            if (json.length() < 10 || json.length() < loadedTicketsCount) {
                Toast.makeText(context, "There are no more tickets", Toast.LENGTH_SHORT).show()
            }
        }

        val loadMoreTicketsBtn: Button = Button(context)
        loadMoreTicketsBtn.text = "Load more tickets..."
        loadMoreTicketsBtn.setTextColor(Color.parseColor("#FFFFFF"))
        loadMoreTicketsBtn.setBackgroundColor(Color.parseColor("#333333"))
        ticketListLayout.addView(loadMoreTicketsBtn)

        loadMoreTicketsBtn.setOnClickListener()
        {
            this.getTicketsByUserIdAndTicketStatus(userId, status, ticketListLayout, context, scrollView, loadedTicketsCount+10)
        }

    }

    fun getTicketByIdAndUserId(id: Int, userId: Int, context: Context): JSONObject {
        var jsonResponse: JSONObject = JSONObject("{}")
        val rs: RequestService = RequestService()
        try {
            val response = rs.getTicketRequest(id, userId)

            jsonResponse = JSONObject("{\"json\": $response}")
            println(jsonResponse)
        }  catch (e: JSONException) {
            Log.e("JSONE", e.toString())
        }

        return jsonResponse
    }

    fun makeTicketView(ticketId: Int, userId: Int, activity: Activity, ticketStatus: Int)
    {

        val backTextView: TextView = activity.findViewById(R.id.back_header_text)
        backTextView.text = "Back"



        val jsonResponse: JSONObject = this.getTicketByIdAndUserId(ticketId, userId, activity.applicationContext)
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
        ticketTopicTextView.text = topic

        /** SET CONTENT TEXT */
        val ticketContentText: TextView = activity.findViewById(R.id.ticketContentText)
        ticketContentText.text = content

        /** SET AUTHOR TEXT */
        val ticketAuthorText: TextView = activity.findViewById(R.id.authorText)
        ticketAuthorText.text = author

        /** SET COMPANY TEXT */
        val companyText: TextView = activity.findViewById(R.id.companyText)
        companyText.text = company

        /** SET PROJECT TEXT */
        val projectText: TextView = activity.findViewById(R.id.projectName)
        projectText.text = project

        val ds = DateService()
        /** PARSE AND SET DEADLINE DATETIME */
        val deadlineDateTime: String = "Deadline: "+ds.parseDateTime(jsonToParse.getJSONObject("deadlineTime"))
        val deadlineTextView: TextView = activity.findViewById(R.id.deadlineText)
        deadlineTextView.text = deadlineDateTime
        /** PARSE AND SET ADDED/CREATED DATETIME */
        val createdDateTime: String = "Added: "+ds.parseDateTime(jsonToParse.getJSONObject("createdTime"))
        val addedTextView: TextView = activity.findViewById(R.id.addedText)
        addedTextView.text = createdDateTime

        /** ATTACHMENTS TO DOWNLOAD */
        val attachments: JSONArray = jsonToParse.getJSONArray("attachments")
        for (i in 0 until attachments.length())
        {
            val fileName: String = attachments[i] as String
            val attachmentsScroll: LinearLayout = activity.findViewById(R.id.attachementsList)
            val attachmentImageButton: ImageButton = ImageButton(activity.applicationContext)

            attachmentImageButton.setImageResource(R.drawable.ic_launcher_foreground)
            attachmentsScroll.addView(attachmentImageButton)
            attachmentImageButton.layoutParams.width = 250
            attachmentsScroll.requestLayout()

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

        /** SHOW MESSAGES */
        val rs = RequestService()
        var messagesResponse = rs.getMessagesRequest(ticketId)
//        println(messagesResponse)
        messagesResponse = messagesResponse.substring(1, messagesResponse.length-1)
        messagesResponse = messagesResponse.replace("\\u0022", "\"")
        val messageJson = JSONObject(messagesResponse)
        println(messageJson)

        val messages = messageJson.getJSONArray("data")
        for (i in 0 until messages.length())
        {
            val messageData = JSONObject(messages[i].toString())
            val senderData = messageData.getJSONObject("recipient")
            val receiverData = messageData.getJSONObject("receiver")
            val message = messageData.getJSONObject("messageData")

            val senderId = senderData.getInt("id")
            val userId = 150
            val messageContent: String = message.getString("message")
            val messageTextView = TextView(activity.applicationContext)
            val messagesBoxLayout = activity.findViewById<LinearLayout>(R.id.messages_box_layout)
            messageTextView.text = messageContent
            messageTextView.setPadding(10, 10, 10, 10)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            if (senderId == userId) {
                messageTextView.setBackgroundColor(Color.parseColor("#333333"))
                messageTextView.setTextColor(Color.parseColor("#FFFFFF"))
                params.setMargins(250, 5, 10, 5)
                messageTextView.gravity = Gravity.END

            } else {
                messageTextView.setBackgroundColor(Color.parseColor("#818181"))
                messageTextView.setTextColor(Color.parseColor("#333333"))
                params.setMargins(10, 5, 250, 5)
                messageTextView.gravity = Gravity.START
            }
            messageTextView.layoutParams = params
            messagesBoxLayout.addView(messageTextView)
        }
    }



    fun createTicket(ticket: Ticket, sp: SharedPreferences)
    {
        val projectName = ticket.getProjectName()
        val topic       = ticket.getTopic()
        val addedUserId = sp.getInt("ID", 0)
        val receivedUserId = ticket.getReceiverId()
        val deadLine = ticket.getDeadline()
        val urgentStatus = ticket.getUrgentStatus()
        val content = ticket.getContent()

        val rs = RequestService()
        val createTicketJsonResponse: JSONObject = JSONObject(rs.createTicketRequest(projectName, topic, addedUserId, receivedUserId, deadLine, urgentStatus, content))

        println(createTicketJsonResponse)

    }

    fun uploadAttachment(activity: Activity)
    {

    }
}