package com.tcenter.tcenter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tcenter.tcenter.service.ChatService
import com.tcenter.tcenter.service.RequestService
import com.tcenter.tcenter.service.TicketsService
import com.tcenter.tcenter.util.FileUtil
import kotlinx.android.synthetic.main.ticket_view.*
import org.json.JSONObject
import java.io.File


class TicketView : AppCompatActivity() {


    @ExperimentalStdlibApi
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
                    val ts = TicketsService()
                    ts.makeTicketView(ticketId, userId, this, ticketStatus)
                }
            } else if(reopeningTicket) {
                if (message == "") {
                    Toast.makeText(applicationContext, "Message cannot be empty", Toast.LENGTH_SHORT).show()
                } else {
                    rs.reopenTicketRequest(ticketId, userId, message)
                    reopenTicketCheckBox.visibility = View.GONE
                    closeTicketCheckbox.visibility  = View.VISIBLE
                    reopenTicketCheckBox.isChecked = false
                    val ts = TicketsService()
                    ts.makeTicketView(ticketId, userId, this, ticketStatus)
                }
            }
            else {

                val rs = RequestService()
                var messagesResponse = rs.getMessagesRequest(ticketId)
                messagesResponse = messagesResponse.substring(1, messagesResponse.length-1)
                messagesResponse = messagesResponse.replace("\\u0022", "\"")
                val messageJson = JSONObject(messagesResponse)
                println(messageJson)
                var receiverId: Int = 150

                val messages = messageJson.getJSONArray("data")
                for (i in 0 until messages.length())
                {
                    val messageData = JSONObject(messages[i].toString())
                    val senderData = messageData.getJSONObject("recipient")
                    val receiverData = messageData.getJSONObject("receiver")
                    val senderId = senderData.getInt("id")


                    if (senderId == userId) {
                        receiverId = receiverData.getInt("id")
                    } else {
                        receiverId = userId
                    }
                    break;
                }


                cs.sendTextMessage(ticketId, receiverId, userId, message, this)
            }
        }

        upload_file_button.setOnClickListener {
            val myFileIntent = Intent(Intent.ACTION_GET_CONTENT)
            myFileIntent.type = "*/*"
            startActivityForResult(myFileIntent, 10)
        }



    }

    @ExperimentalStdlibApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            val fileUri: Uri = data!!.data!!
            val fileUriString = fileUri.toString()
            val myFile = File(FileUtil.getPath(fileUri, applicationContext))
            val path: String = myFile.absolutePath
            var displayName: String? = null
           println("FILE URI:"+fileUriString)

            if (fileUriString.startsWith("content://")) {
                var cursor: Cursor? = null
                try {
                    cursor = this.getContentResolver().query(fileUri, null, null, null, null)
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName =
                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        println("DISPLAY: $displayName")
                    }
                } finally {
                    cursor?.close()
                }
            } else if (fileUriString.startsWith("file://")) {
                displayName = myFile.name
                println("DISPLAY: $displayName")
            }

            val b: Bundle = intent.extras!!

            val ticketId: Int = b.getInt("id")
            val userId: Int   = b.getInt("userId")
            val ticketStatus: Int = b.getInt("ticketStatus")
            val receiverId: Int = b.getInt("receiverId")
            val ts = TicketsService()
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                ts.uploadAttachment(myFile, displayName, ticketId, receiverId, userId, this)
            } else {
                // Request permission from the user
                ActivityCompat.requestPermissions(this,
                    arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}