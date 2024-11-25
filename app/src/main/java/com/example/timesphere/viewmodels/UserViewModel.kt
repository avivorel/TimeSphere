package com.example.timesphere.viewmodels

import androidx.lifecycle.ViewModel
import com.example.timesphere.model.User

class UserViewModel : ViewModel(){
    private var user : User? = null





    fun getUser() : User?{
        return user
    }
}