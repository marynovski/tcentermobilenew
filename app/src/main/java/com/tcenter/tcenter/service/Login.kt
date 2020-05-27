package com.tcenter.tcenter.service

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.webkit.JsPromptResult
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject

class Login {

    private fun saveUserDataToPrefs(user_data: JSONObject, preferences: SharedPreferences)
    {
        println(user_data)


        //USER DATA
        val name    = user_data.getString("name")
        val surname = user_data.getString("surname")
        val phone   = user_data.getString("phone")

        val editor = preferences.edit()
        editor.putBoolean("IS_AUTHENTICATED", true)
        editor.putInt("ID", 150)
        editor.putString("NAME", name)
        editor.putString("SURNAME", surname)
        editor.putString("PHONE", phone)
        editor.apply()
    }

    fun login(username: String, password: String, context: Context, preferences: SharedPreferences)
    {
        println("LOGIN SERVICE: $username $password")
        val rs = RequestService()
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
                println("$code : $message")
                val user_data: JSONObject  = JSONObject(json.getString("user_data"))
                this.saveUserDataToPrefs(user_data, preferences)
            }
            else -> { // Note the block
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }

    }
}