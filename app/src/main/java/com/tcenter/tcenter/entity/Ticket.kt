package com.tcenter.tcenter.entity

class Ticket(
    private val topic: String,
    private val projectName: String,
    private val receiverId: Int,
    private val deadline: String,
    private val urgent: Boolean,
    private val content: String
) {
    fun getTopic(): String {
        return this.topic
    }

    fun getProjectName(): String {
        return  this.projectName
    }

    fun getReceiverId(): Int {
        return this.receiverId
    }

    fun getDeadline(): String {
        return this.deadline
    }

    fun getUrgentStatus(): Boolean {
        return this.urgent
    }

    fun getContent(): String {
        return this.content
    }

}