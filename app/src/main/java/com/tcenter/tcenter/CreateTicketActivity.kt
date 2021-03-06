package com.tcenter.tcenter

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.tcenter.tcenter.entity.Ticket
import com.tcenter.tcenter.service.RequestService
import com.tcenter.tcenter.service.TicketsService
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CreateTicketActivity : AppCompatActivity() {
    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_ticket_view)

        val sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE)

        val projectNameInput: EditText = findViewById(R.id.projectNameInput)
        val topicInput: EditText = findViewById(R.id.topicInput)
        val receiverUserInput: EditText = findViewById(R.id.receiverUserInput)
        val deadlineInput: TextView = findViewById(R.id.deadlineInput)
        val urgentCheckBox: CheckBox = findViewById(R.id.urgentCheckbox)
        val contentInput: EditText = findViewById(R.id.contentInput)
        val deadlineDatePicker: DatePicker = findViewById(R.id.deadlineDatePicker)
        val submitBtn: Button = findViewById(R.id.submitBtn)
        val backBtn: ImageView = findViewById(R.id.back_btn)

        backBtn.setOnClickListener {
            this.redirectToTicketListActivity()
        }

        val deadlineDateFormat = SimpleDateFormat("YYYY-MM-dd")
        val deadlineTimeFormat = SimpleDateFormat("HH:mm")
        var deadlineDateTime: String = ""
        deadlineInput.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val date = deadlineDateFormat.format(selectedDate.time)
                Toast.makeText(applicationContext, date, Toast.LENGTH_SHORT).show()
                deadlineDateTime = date

                val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minute)
                    val time = deadlineTimeFormat.format(selectedTime.time)
                    deadlineDateTime += " "+time+":00"
                    deadlineInput.text = deadlineDateTime
                },
                    now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true)
                    timePicker.show()
            },
            now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }


        val ts = TicketsService()
        submitBtn.setOnClickListener {
            val topic: String              = topicInput.text.toString()
            val project: String            = projectNameInput.text.toString()
            val receiverString: String     = receiverUserInput.text.toString()
            val receiverData: List<String> = receiverString.split(" ")
            val receiver: Int              = receiverData[3].toInt()
            val deadline: String           = deadlineInput.text.toString()
            val urgent: Boolean            = urgentCheckBox.isChecked
            val content: String            = contentInput.text.toString()

            val ticket = Ticket(topic, project, receiver, deadline, urgent, content)
            ts.createTicket(ticket, sharedPreferences)
        }

        val rs = RequestService()
        val active_users_response: JSONObject = JSONObject(rs.getActiveUsersRequest())
        val active_users: JSONArray = active_users_response.getJSONArray("response")
        var suggestions = mutableListOf<String>()

        for (i in 0 until active_users.length()) {
            val user: JSONObject = active_users.getJSONObject(i)
            val id = user.get("id")
            val fullName = user.get("fullName")
            val phone = user.get("phone")
            val userString = "$fullName $phone $id"

            suggestions.add(i, userString)

        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, suggestions)
        val autocompletetextview: AutoCompleteTextView = findViewById(R.id.receiverUserInput)
        autocompletetextview.threshold = 0
        autocompletetextview.setAdapter(adapter)
        autocompletetextview.setOnFocusChangeListener { v, hasFocus -> if(hasFocus) autocompletetextview.showDropDown() }
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