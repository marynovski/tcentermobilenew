package com.tcenter.tcenter.service

import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class RequestService {

    val TCENTER_API_URL = "https://www.tcenter.pl/api/v/mobile/"

    fun loginRequestJob(username: String, password: String) = runBlocking()
    {
        val json = "{\"params\":{\"username\":\"0048731041224\",\"password\":\"4M6DOFG7\"}}"
        var jsonResponse: String = "ERROR"

        /** http://www.tcenter.pl/api/v/mobile/login */
        val url: URL = URL("http://www.tcenter.pl/api/v/mobile/login")
        try {
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json; utf-8")
            conn.setRequestProperty("Charset", "utf-8")
            conn.setRequestProperty("Accept", "application/json")
            conn.setRequestProperty("Authorization", "7f137082d82368af5968aac4150b3854644b5957")
            conn.doOutput = true

            conn.outputStream.use { os ->
                val input = json.toByteArray(charset("utf-8"))
                os.write(input, 0, input.size)
            }

            BufferedReader(
                InputStreamReader(conn.inputStream, "utf-8")
            ).use { br ->
                val response = StringBuilder()
                var responseLine: String? = null
                while (br.readLine().also { responseLine = it } != null) {
                    response.append(responseLine!!.trim { it <= ' ' })
                }
                jsonResponse = response.toString()
                conn.disconnect()
            }


        } catch (e: MalformedURLException) {
            e.printStackTrace();
        }
        catch (e: JSONException) {
            e.printStackTrace();
        }
        catch (e: IOException) {
            e.printStackTrace();
        }

        return@runBlocking jsonResponse
    }

    fun loginRequest(username: String, password: String) : String
    {
        println("START LOGIN REQUEST")
        var jsonResponse: String = "ERROR LOGIN REQUEST"
        runBlocking {
            val getLoginResponseJob = async(Dispatchers.IO) { loginRequestJob(username, password) }

            runBlocking(block = {
                jsonResponse = getLoginResponseJob.await()
            })
        }

        println("FINISH LOGIN REQUEST")
        return jsonResponse
    }
}