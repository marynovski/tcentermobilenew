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

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkIfDeadlineIsOver(dateTime: JSONObject) : Boolean
    {
//        2020-06-16T12:59:42.344
        val currentDateTime = LocalDateTime.now().toString()
        val date_and_time: List<String> = currentDateTime.split("T")
        val date: String = date_and_time[0]
        val time: String = date_and_time[1]

        val year_month_day: List<String> = date.split("-")
        val hour_minute_second: List<String> = time.split(":", ".")

        val year: Int = year_month_day[0].toInt()
        val month: Int = year_month_day[1].toInt()
        val day: Int = year_month_day[2].toInt()

        val hour: Int = hour_minute_second[0].toInt()
        val minute: Int = hour_minute_second[1].toInt()
        val second: Int = hour_minute_second[2].toInt()

        val dateString: String = dateTime.getString("date")
        val date_and_time2: List<String> = dateString.split(" ")
        val date2: String = date_and_time2[0]
        val time2: String = date_and_time2[1]
        val year_month_day2: List<String> = date.split("-")
        val hours_minutes_seconds2: List<String> = time.split(":")
        val year2: Int = year_month_day2[0].toInt()
        val monthNumber2: Int = year_month_day[1].toInt()
        val day2: Int = year_month_day[2].toInt()
        val hour2: Int = hours_minutes_seconds2[0].toInt()
        val minute2: Int = hours_minutes_seconds2[1].toInt()
        var month2: Int = monthNumber2.toInt()

        var isOver = false

        if (year > year2) {
            isOver = true
        } else if (month > month2) {
            isOver = true
        } else if (day > day2) {
            isOver = true
        } else if (hour > hour2) {
            isOver = true
        } else if (minute > minute2){
            isOver = true
        }
    }
}