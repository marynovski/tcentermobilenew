package com.tcenter.tcenter.service

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.webkit.JsPromptResult
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject

class Login {

    private fun translateDirectNumber(directNumber: String): String
    {
        val length: Int = directNumber.length
        val direct_number_split: List<String>
        var translatedDirectNumber = "0048"

        if (length == 3) {
            direct_number_split = directNumber.split("+")
            translatedDirectNumber = "00"+direct_number_split[1]

        } else if (length == 4) {
            direct_number_split = directNumber.split("+")
            translatedDirectNumber = "0"+direct_number_split[1]
        }

        println(translatedDirectNumber)

        return translatedDirectNumber
    }

    private fun saveUserDataToPrefs(user_data: JSONObject, preferences: SharedPreferences)
    {
        //USER DATA
        val id      = user_data.getInt("id")
        val name    = user_data.getString("name")
        val surname = user_data.getString("surname")
        val phone   = user_data.getString("phone")

        val editor = preferences.edit()
        editor.putBoolean("IS_AUTHENTICATED", true)
        editor.putInt("ID", id)
        editor.putString("NAME", name)
        editor.putString("SURNAME", surname)
        editor.putString("PHONE", phone)
        editor.apply()
    }

    fun login(directNumber: String, phone: String, password: String, context: Context, preferences: SharedPreferences) : Int
    {

        val translatedDirectNumber: String = this.translateDirectNumber(directNumber)
        val username: String = translatedDirectNumber+phone

        println("USERNAME: $username")

        val rs = RequestService()
        println(password.toString())
        var response = rs.loginRequest(username, password)

        /** PARSE JSON */
        var jsonResponse: JSONObject = JSONObject("{}")

        try {
            jsonResponse = JSONObject("{\"json\": $response}")
            println(jsonResponse)
        }  catch (e: JSONException) {
            Log.e("JSONE", e.toString());
        }


        val json = JSONObject(jsonResponse.getString("json"))
        val status: JSONObject    = JSONObject(json.getString("status"))
        val code: String          = status.getString("code")
        val message: String       = status.getString("message")

        /** REDIRECT PROGRAM IF AUTH IS SUCCESS -> 1 OR NOT -> 0 */
        when (code) {
            "0" -> {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
            "1" -> {
                val user_data: JSONObject  = JSONObject(json.getString("user_data"))
                this.saveUserDataToPrefs(user_data, preferences)

            }
            else -> { // Note the block
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

        return code.toInt()
    }
}