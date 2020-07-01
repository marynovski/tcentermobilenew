package com.tcenter.tcenter.service

import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.time.LocalDateTime

class DateService {

    fun parseDateTime(dateTime: JSONObject): String
    {
        val dateString: String = dateTime.getString("date")
        val date_and_time: List<String> = dateString.split(" ")
        val date: String = date_and_time[0]
        val time: String = date_and_time[1]
        val year_month_day: List<String> = date.split("-")
        val hours_minutes_seconds: List<String> = time.split(":")
        val monthNumber: String = year_month_day[1]
        val day: String = year_month_day[2]
        val hour: String = hours_minutes_seconds[0]
        val minute: String = hours_minutes_seconds[1]
        var month: String = monthNumber

        println(hours_minutes_seconds)
        println(hour)
        println(minute)

        when(monthNumber) {
            "01" -> month = "JAN"
            "02" -> month = "FEB"
            "03" -> month = "MAR"
            "04" -> month = "APR"
            "05" -> month = "MAY"
            "06" -> month = "JUN"
            "07" -> month = "JUL"
            "08" -> month = "AUG"
            "09" -> month = "SEP"
            "10" -> month = "OCT"
            "11" -> month = "NOV"
            "12" -> month = "DEC"
        }


        return "$day $month $hour:$minute"
    }

    fun checkIfDeadlineIsOver(dateTime: JSONObject) : Boolean
    {


        return true
    }
}