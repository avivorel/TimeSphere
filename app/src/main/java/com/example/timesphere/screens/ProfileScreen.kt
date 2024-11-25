package com.example.timesphere.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun ProfileScreen(){
    val title by remember{ mutableStateOf("Profile screen") }

    Column() {
        Text(title)
    }
}