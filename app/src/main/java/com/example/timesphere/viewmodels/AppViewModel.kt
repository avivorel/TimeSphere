package com.example.timesphere.viewmodels

import AuthRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel(){
    var employerId by mutableStateOf("Employer given ID")
    var email by mutableStateOf("Your email")
    var id by mutableStateOf("Israeli ID number")
    var authRepository: AuthRepository = AuthRepository()

    fun validateUser(id: String, email: String, employerId: String,employeeId: String, onResult: (Boolean) -> Unit){
        authRepository.checkUserInFirestore(id = id,email = email,employerId = employerId,employeeId = employeeId,onResult = onResult)
    }

}