package com.example.timesphere.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timesphere.ui.theme.ClockInButton
import com.example.timesphere.ui.theme.RoundedCornerCardTop

@Composable
fun HomeScreen(){
    val title by remember{ mutableStateOf("Hi Username") }

    Column(modifier = Modifier.fillMaxSize()
        .background(Color(0xFFF5F5F5)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {
        Row(modifier = Modifier.weight(1f).
            padding(top= 75.dp),
            horizontalArrangement = Arrangement.Center) {
            Text(title,
                fontSize = 40.sp)
        }
        Row(modifier= Modifier.weight(3f)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            RoundedCornerCardTop(content = {
                Column(modifier = Modifier
                    .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    ClockInButton(text = "Clock In")
                }
            }, modifier = Modifier.fillMaxSize(), onClick = {  }) {

            }
        }
    }
}

@Preview
@Composable
fun PrevHomeScreen(){
    HomeScreen()
}