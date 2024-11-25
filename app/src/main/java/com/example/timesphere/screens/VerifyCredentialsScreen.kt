package com.example.timesphere.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timesphere.ui.theme.RoundedCornerCardTop
import com.example.timesphere.viewmodels.CredentialsViewModel

@Composable
fun VerifyCredentials(){
    val credsViewModel : CredentialsViewModel = viewModel()
    val headline by remember {mutableStateOf("Verify Credentials")}
    val headlineSubtext by remember { mutableStateOf(
        """Those were given to us by your Employer.
            |If you see anything wrong here,
            |please contact your Immediate supervisor.
        """.trimMargin()
    ) }
    val note by remember { mutableStateOf("""
        Please note!
        Some of the fields can't be modifier
        due to the employers policy.
    """.trimIndent()) }
    val firstName by remember{ mutableStateOf("First name") }
    val lastName by remember{ mutableStateOf("Last name") }
    val id by remember{ mutableStateOf("ID") }
    val phoneNumber by remember{ mutableStateOf("054-1234567") }
    val email by remember{ mutableStateOf("example@gmail.com") }
    val hourlyPay by remember{ mutableDoubleStateOf(100.00) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(text = headline,
                textAlign = TextAlign.Center)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(text = headlineSubtext,textAlign = TextAlign.Center)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 25.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(text = note,textAlign = TextAlign.Center)
        }
        Row(modifier = Modifier
            .weight(3f)
            .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){
            RoundedCornerCardTop(
                content = {
                    Column(modifier = Modifier
                        .fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Row(modifier= Modifier
                            .padding(top= 30.dp, bottom = 50.dp)
                        ){
                            Text(
                                "Login",
                                modifier = Modifier,
                                fontSize = 35.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                        Row(modifier= Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
                            Spacer(modifier = Modifier.weight(0.25f))
                            TextField(modifier = Modifier.weight(1f),
                                textStyle = TextStyle(textAlign = TextAlign.Center),
                                value = firstName,
                                onValueChange = {
                                },
                                enabled = false
                            )
                            Spacer(modifier = Modifier.weight(0.5f))
                            TextField(modifier = Modifier.weight(1f),
                                textStyle = TextStyle(textAlign = TextAlign.Center),
                                value = lastName,
                                onValueChange = {
                                },
                                enabled = false
                            )
                            Spacer(modifier = Modifier.weight(0.25f))
                        }
                        Row(modifier= Modifier
                            .padding(top= 20.dp, bottom = 20.dp)
                        ){
                            Spacer(modifier = Modifier.weight(0.25f))
                            TextField(modifier = Modifier.weight(1f),
                                textStyle = TextStyle(textAlign = TextAlign.Center),
                                value = id,
                                onValueChange = {
                                },
                                enabled = false
                            )
                            Spacer(modifier = Modifier.weight(0.5f))
                            TextField(modifier = Modifier.weight(1f),
                                textStyle = TextStyle(textAlign = TextAlign.Center),
                                value = phoneNumber,
                                onValueChange = {
                                },
                                enabled = false
                            )
                            Spacer(modifier = Modifier.weight(0.25f))
                        }
                        Row(modifier= Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .fillMaxWidth()
                            , horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Spacer(modifier = Modifier.weight(0.25f))
                            TextField(modifier = Modifier.weight(1f),
                                textStyle = TextStyle(textAlign = TextAlign.Center),
                                value = email,
                                onValueChange = {
                                },
                                enabled = false
                            )
                            Spacer(modifier = Modifier.weight(0.25f))
                        }
                        Row(modifier= Modifier
                            .padding(top= 20.dp, bottom = 20.dp)
                            , horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Spacer(modifier = Modifier.weight(0.25f))
                            TextField(modifier = Modifier.weight(1f),
                                textStyle = TextStyle(textAlign = TextAlign.Center),
                                value = hourlyPay.toString(),
                                onValueChange = {
                                },
                                enabled = false
                            )
                            Checkbox(checked = true, onCheckedChange = {}, enabled = false )
                            Text("Extra Hours")
                            Spacer(modifier = Modifier.weight(0.25f))
                        }
                        Row(modifier= Modifier
                            .padding(top= 20.dp, bottom = 20.dp)
                            , horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Button(onClick = {}){
                                Text("Verify")
                            }
                        }
                    }
                }, modifier = Modifier.background(Color.Transparent),
                onClick = {

                }) {

            }
        }
    }
}



@Preview
@Composable
fun preview(){
    VerifyCredentials()
}