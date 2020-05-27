package com.tcenter.tcenter.service

class Login {

    fun login(username: String, password: String)
    {
        println("LOGIN SERVICE: $username $password")
        val rs = RequestService()
        val response = rs.loginRequest(username, password)

        println(response)
    }
}