package com.example.timesphere.model

import FirebaseRepository
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class Shift(    var shiftId: String = "",
                     var employeeId: String = "",
                     var date: String = "",
                     var startTime: Timestamp = Timestamp(0, 0),
                     var endTime: Timestamp = Timestamp(0, 0),
                     var hoursWorked: String = "00:00",
                     var isComplete: Boolean = false
) {
    private val utils = Utils()
    private val firebase = FirebaseRepository()

    fun getDay(): String{
        return utils.formatTimestampToDayAndDate(startTime).first
    }
    fun getStartTime() : String{
        return utils.formatTimestampToTime(startTime)
    }
    fun getEndTime() : String{
        return if (endTime != Timestamp(0, 0)) utils.formatTimestampToTime(endTime) else ""
    }

    fun updateTimestamp(newTime: String,isEnd:Boolean, onResult: (Boolean) -> Unit) {

        val (newHour, newMinute) = newTime.split(":").map { it.toInt() }
        val currentDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(startTime.seconds),
            ZoneId.systemDefault()
        )
        val updatedDateTime = currentDateTime.withHour(newHour).withMinute(newMinute)
        val updatedInstant = updatedDateTime.atZone(ZoneId.systemDefault()).toInstant()
        startTime = Timestamp(updatedInstant.epochSecond, updatedInstant.nano)
        firebase.updateShiftStartTime(this,startTime,isEnd){ isSuccessful ->
            onResult(isSuccessful)
        }
    }
    fun canModify() : Boolean = endTime != Timestamp(0,0)


}