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
        var json = "{\"username\":\"$username\",\"password\":\"$password\"}"
        var jsonResponse: String = "{\"user_data\":[],\"status\":{\"code\":0,\"message\":\"Check your network connection\"}}"

        /** http://www.tcenter.pl/api/v/mobile/login */
        val url: URL = URL("http://188.68.224.36:8081/api/v/mobile/login")
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
        var jsonResponse: String = "{}"
        runBlocking {
            val getLoginResponseJob = async(Dispatchers.IO) { loginRequestJob(username, password) }

            runBlocking(block = {
                jsonResponse = getLoginResponseJob.await()
            })
        }

        println("FINISH LOGIN REQUEST")
        return jsonResponse
    }

    fun getTicketsRequestJob(userId: Int, status: Int, offset: Int, limit: Int) = runBlocking()
    {
        val json = "{\"params\":{\"userId\":\"$userId\",\"status\":\"$status\",\"offset\":\"1\",\"limit\":\"$limit\"}}"
        var jsonResponse: String = "{}"

        /** http://www.tcenter.pl/api/v/mobile/get-tickets */
        val url: URL = URL("http://www.tcenter.pl/api/v/mobile/get-tickets")
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

    fun getTicketsRequest(userId: Int, status: Int, offset: Int, limit: Int): String
    {
        println("START GET TICKETS REQUEST")
        var jsonResponse: String = "ERROR LOGIN REQUEST"
        runBlocking {
            val getTicketsRequestJob = async(Dispatchers.IO) { getTicketsRequestJob(userId, status, offset, limit) }

            runBlocking(block = {
                jsonResponse = getTicketsRequestJob.await()
            })
        }

        println("FINISH GET TICKETS REQUEST")
        return jsonResponse
    }

    fun getTicketRequestJob(id: Int, userId: Int) = runBlocking()
    {
        val json = "{\"params\":{\"ticketId\":\"$id\",\"userId\":\"$userId\"}}"
        var jsonResponse: String = "{}"

        /** http://www.tcenter.pl/api/v/mobile/get-ticket */
        val url: URL = URL("http://www.tcenter.pl/api/v/mobile/get-ticket")
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

    fun getTicketRequest(id: Int, userId: Int): String
    {
        println("START LOGIN REQUEST")
        var jsonResponse: String = "{}"
        runBlocking {
            val getTicketRequestJob = async(Dispatchers.IO) { getTicketRequestJob(id, userId) }

            runBlocking(block = {
                jsonResponse = getTicketRequestJob.await()
            })
        }

        println("FINISH LOGIN REQUEST")
        return jsonResponse
    }
}