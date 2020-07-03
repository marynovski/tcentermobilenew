package com.tcenter.tcenter.service

import org.json.JSONObject
import java.time.LocalDateTime
import java.util.*

class DateService {

    private fun convertMonthNumberToString(monthNumber: String) : String
    {
        var month: String = "JAN"

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

        return month
    }

    private fun converStringMonthToNumber(monthString: String) : Int
    {
        var month: Int = 1

        when(monthString) {
            "Jan" -> month = 1
            "Feb" -> month = 2
            "Mar" -> month = 3
            "Apr" -> month = 4
            "May" -> month = 5
            "Jun" -> month = 6
            "Jul" -> month = 7
            "Aug" -> month = 8
            "Sep" -> month = 9
            "Oct" -> month = 10
            "Nov" -> month = 11
            "Dec" -> month = 12
        }

        return month
    }


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

        month = this.convertMonthNumberToString(monthNumber)


        return "$day $month $hour:$minute"
    }

    fun checkIfDeadlineIsOver(dateTime: JSONObject) : Boolean
    {
        val calendar: Calendar = Calendar.getInstance()
        val actualDateTime: String = calendar.time.toString()
        println(actualDateTime)

        val splited_datetime = actualDateTime.split(" ")
        val month: String = splited_datetime[1]
        val day: Int = splited_datetime[2].toInt()
        val time: String = splited_datetime[3]
        val hour_minute_second: List<String> = time.split(":")
        val hour: Int = hour_minute_second[0].toInt()
        val minute: Int = hour_minute_second[1].toInt()
        val year: Int = splited_datetime[5].toInt()

        val dateString: String = dateTime.getString("date")
        val date_and_time2: List<String> = dateString.split(" ")
        val date2: String = date_and_time2[0]
        val time2: String = date_and_time2[1]
        val year_month_day2: List<String> = date2.split("-")
        val hours_minutes_seconds2: List<String> = time2.split(":")
        val year2: Int = year_month_day2[0].toInt()
        val monthNumber2: String = year_month_day2[1]
        val day2: Int = year_month_day2[2].toInt()
        val hour2: Int = hours_minutes_seconds2[0].toInt()
        val minute2: Int = hours_minutes_seconds2[1].toInt()
        var month2: Int = monthNumber2.toInt()

        val monthNumber: Int = this.converStringMonthToNumber(month)


        var isOver = false

        if (year2 < year) {
            isOver = true
        } else if(year2 == year && month2 < monthNumber ) {
            isOver = true
        } else if(year2 == year && month2 == monthNumber && day2 < day){
            isOver = true
        } else if(year2 == year && month2 == monthNumber && day2 < day) {
            isOver = true
        } else if(year2 == year && month2 == monthNumber && day2 == day && minute2 < minute) {
            isOver = true
        }

        println("$year-$monthNumber-$day $hour:$minute")
        println("$year2-$month2-$day2 $hour2:$minute2")

        return isOver
    }
}