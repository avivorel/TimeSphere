package com.example.timesphere.model

import com.google.firebase.Timestamp


data class User(
    var uid: String = "-1",
    var email: String = "example@gmail.com",
    var firstName: String = "firstName",
    var lastName: String = "lastName",
    var isEmployer: Boolean = false,
    var hourlyPay : Double = 0.00,
    var employerId :String = "Employer ID",
    var id : String = "Israeli issued ID",
    var phoneNumber : String = "054-1234567",
    var inShift : Boolean  =false,
    var shiftStarted : Timestamp = Timestamp(0,0)
)