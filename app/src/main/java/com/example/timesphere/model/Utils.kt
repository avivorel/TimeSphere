package com.example.timesphere.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

class Utils{
    fun formatTimestampToDayAndDate(startTime: Timestamp): Pair<String, String> {
        // Convert Timestamp to Date
        val date = startTime.toDate()

        // Define the formatter for the day of the week
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault()) // Example: Mon, Tues
        val dayOfWeek = dayFormat.format(date)

        // Define the formatter for the date in "day/month" format
        val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault()) // Example: 30/11
        val formattedDate = dateFormat.format(date)

        return Pair(dayOfWeek, formattedDate)
    }

    fun processShiftDocuments(querySnapshot: QuerySnapshot): List<Shift> {
        return querySnapshot.documents.mapNotNull { document ->
            try {
                val shiftId = document.id
                val employeeId = document.getString("employeeId") ?: ""
                val startTime = document.getTimestamp("timeStarted") ?: Timestamp(0, 0)
                val endTime = document.getTimestamp("timeFinished") ?: Timestamp(0, 0)
                val hoursWorked = getShiftTime(startTime,endTime)
                val (day, date) = formatTimestampToDayAndDate(document.getTimestamp("timeStarted")!!)
                Shift(
                    shiftId = shiftId,
                    employeeId = employeeId,
                    date = date,
                    startTime = startTime,
                    endTime = endTime,
                    hoursWorked = hoursWorked,
                    isComplete = endTime != Timestamp(0, 0),
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null // Skip invalid documents
            }
        }
    }

    fun formatTimestampToTime(startTime: Timestamp): String {
        val date = startTime.toDate()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault()) // 24-hour format
        return timeFormat.format(date)
    }
    private fun getShiftTime(startTime: Timestamp, endTime :Timestamp) :String{
        if (endTime == Timestamp(0,0)){
            return ""
        }
        val durationInMillis = endTime.toDate().time - startTime.toDate().time

        // Convert the duration to hours and minutes
        val hours = TimeUnit.MILLISECONDS.toHours(durationInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis) % 60

        // Return in Hrs:Min format
        return String.format("%02d:%02d", hours, minutes)
    }
    fun calculateMoneyMadeInShift(shift: Shift, hourlyRate: Double): Double {
        if (shift.hoursWorked != "") {
            // Extract hours and minutes from the "hoursWorked" string
            val (hours, minutes) = shift.hoursWorked.split(":").map { it.toIntOrNull() ?: 0 }

            // Convert total time to a decimal hour format
            val totalHours = hours + (minutes / 60.0)

            // Calculate the money made
            return totalHours * hourlyRate
        }
        return 0.00
    }

    fun calculateShiftDuration(shiftStarted: Timestamp): String {
        val shiftStartMillis = shiftStarted.toDate().time
        val currentMillis = System.currentTimeMillis()
        val durationMillis = currentMillis - shiftStartMillis
        val hours = TimeUnit.MILLISECONDS.toHours(durationMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) % 60
        if(hours > 200){
            return "0:00"
        }
        if (hours >= 10) {
            return String.format("%02d:%02d", hours, minutes)
        }
        return String.format("%01d:%02d", hours, minutes)
    }
}