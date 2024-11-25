package com.example.timesphere.model


data class User(
    val uid: String = "",
    val email: String = "example@gmail.com",
    val firstName: String = "firstName",
    val lastName: String = "lastName",
    val isEmployer: Boolean = false,
    val hourlyPay : Double = 0.00,
    val issuedId : Long = -1,
)