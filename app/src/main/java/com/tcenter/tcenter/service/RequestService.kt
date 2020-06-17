package com.tcenter.tcenter.service

import android.content.Context
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.json.JSONException
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class RequestService {

    val TCENTER_API_URL = "https://www.tcenter.pl/api/v/mobile/"

    fun loginRequestJob(username: String, password: String) = runBlocking()
    {
        var json = "{\"params\":{\"username\":\"$username\",\"password\":\"$password\"}}"
        var jsonResponse: String = "{\"user_data\":[],\"status\":{\"code\":0,\"message\":\"Check your network connection\"}}"

        /** http://www.tcenter.pl/api/v/mobile/login */
        val url: URL = URL("http://188.68.224.36:8194/api/v/mobile/login")
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
            e.printStackTrace()
        }
        catch (e: JSONException) {
            e.printStackTrace()
        }
        catch (e: IOException) {
            e.printStackTrace()
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
        val json = "{\"params\":{\"userId\":\"$userId\",\"status\":\"$status\",\"offset\":\"0\",\"limit\":\"$limit\"}}"
        println("REQUEST: $json")
        var jsonResponse: String = "{}"

        /** http://www.tcenter.pl/api/v/mobile/get-tickets */
        val url: URL = URL("http://188.68.224.36:8194/api/v/mobile/get-tickets")
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
            e.printStackTrace()
        }
        catch (e: JSONException) {
            e.printStackTrace()
        }
        catch (e: IOException) {
            e.printStackTrace()
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
        val url: URL = URL("http://188.68.224.36:8194/api/v/mobile/get-ticket")
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
            e.printStackTrace()
        }
        catch (e: JSONException) {
            e.printStackTrace()
        }
        catch (e: IOException) {
            e.printStackTrace()
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


    fun downloadAttachementRequest(fileName: String, context: Context): String {
        println("START LOGIN REQUEST")
        var jsonResponse: String = "{}"
        runBlocking {
            val getdownloadAttachementJob = async(Dispatchers.IO) { downloadAttachementJob(fileName, context) }

            runBlocking(block = {
                jsonResponse = getdownloadAttachementJob.await()
            })
        }

        println("FINISH LOGIN REQUEST")
        println(jsonResponse)
        return jsonResponse
    }

    fun downloadAttachementJob(fileName: String, context: Context) = runBlocking {
        val json = "{}"
        var jsonResponse: String = "{}"

        /** http://www.tcenter.pl/api/v/mobile/get-ticket */
        val url: URL = URL("http://188.68.224.36:8194/api/v/mobile/attachments/$fileName")
        try {
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json; utf-8")
            conn.setRequestProperty("Charset", "utf-8")
            conn.setRequestProperty("Accept", "application/json")
            conn.setRequestProperty("Authorization", "7f137082d82368af5968aac4150b3854644b5957")
            conn.doOutput = true

            val destination: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(destination, "$fileName")

            val fileOutput = FileOutputStream(file)
            val inputStream: InputStream = conn.getInputStream()

            val buffer = ByteArray(1024)
            var bufferLength = 0

            while (inputStream.read(buffer).also({ bufferLength = it }) > 0) {
                fileOutput.write(buffer, 0, bufferLength)
            }
            fileOutput.close()

        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        catch (e: JSONException) {
            e.printStackTrace()
        }
        catch (e: IOException) {
            e.printStackTrace()
        }

        return@runBlocking jsonResponse
    }

    fun closeTicketRequest(ticketId: Int, userId: Int): String {
        println("START CLOSE TICKET REQUEST")
        var jsonResponse: String = "{}"
        runBlocking {
            val getCloseTicketJob = async(Dispatchers.IO) { closeTicketJob(ticketId, userId) }

            runBlocking(block = {
                jsonResponse = getCloseTicketJob.await()
            })
        }

        println("FINISH CLOSE TICKET REQUEST")
        return jsonResponse
    }

    fun closeTicketJob(ticketId: Int, userId: Int) = runBlocking()
    {
        val json = "{\"params\":{\"ticketId\":\"$ticketId\",\"userId\":\"$userId\",\"message\":\"Ticket closed via Tcenter Mobile\"}}"
        var jsonResponse: String = "{}"

        /** http://www.tcenter.pl/api/v/mobile/get-ticket */
        val url: URL = URL("http://188.68.224.36:8194/api/v/mobile/close-ticket")
        try {
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json; utf-8")
            conn.setRequestProperty("Charset", "utf-8")
            conn.setRequestProperty("Accept", "application/json")
            conn.setRequestProperty("Authorization", "7f137082d82368af5968aac4150b3854644b5957")
            conn.doOutput = true
            conn.doInput = true

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
            e.printStackTrace()
        }
        catch (e: JSONException) {
            e.printStackTrace()
        }
        catch (e: IOException) {
            e.printStackTrace()
        }

        return@runBlocking jsonResponse
    }
}