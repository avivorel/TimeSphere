package com.example.timesphere.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.timesphere.ui.theme.RoundedCornerCardTop
import com.example.timesphere.viewmodels.AppViewModel




@Composable
fun WelcomeScreen(){
    val TAG = "WelcomeScreen"
    val headlineText by remember {mutableStateOf("Welcome to")}
    val appName by remember { mutableStateOf("TimeSphere") }
    var navHost = rememberNavController()
    var appViewModel : AppViewModel = viewModel()
    val numberRegexPattern = remember { Regex("^\\d+\$") }

    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(headlineText,
                modifier = Modifier,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif)
        }
        Row(
            modifier = Modifier
                .weight(0.75f)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(appName,
                modifier = Modifier,
                fontSize = 35.sp,
                fontFamily = FontFamily.SansSerif)
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
                            .padding(top= 20.dp, bottom = 20.dp)
                        ){
                            TextField(label={Text("Employer Id")},
                                value = appViewModel.employerId,
                                modifier = Modifier.clickable {
                                    if(appViewModel.employerId == "Employer given ID"){
                                        appViewModel.employerId = ""
                                    }
                                },
                                onValueChange = {
                                    appViewModel.employerId = it
                                    Log.d(TAG,"employerId value: ${appViewModel.employerId}")
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                        Row(modifier= Modifier
                            .padding(top= 20.dp, bottom = 20.dp)
                        ){
                            TextField(label={Text("Email")},
                                value = appViewModel.email,
                                onValueChange = {
                                    appViewModel.email = it
                                        Log.d(TAG,"email value: ${appViewModel.email}")
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                        Row(modifier= Modifier
                            .padding(top= 20.dp, bottom = 20.dp)
                        ){
                            TextField(label={Text("תעודת זהות")},
                                value = appViewModel.id,
                                onValueChange = {
                                    appViewModel.id = it
                                        Log.d(TAG,"userIdText value: ${appViewModel.id}")
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                        Row(modifier= Modifier
                            .padding(top= 20.dp, bottom = 20.dp)
                            , horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Checkbox(checked = false, onCheckedChange = {})
                            Text("Allow FaceID")
                        }
                        Row(modifier= Modifier
                            .padding(top= 20.dp, bottom = 20.dp)
                            , horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Button(onClick = {
                                appViewModel.validateUser(id= appViewModel.id,email = appViewModel.email, employerId = appViewModel.employerId, employeeId = "1"){ isSuccessful->
                                    if (isSuccessful){
                                        Toast.makeText(context,"foundUser", Toast.LENGTH_LONG).show()
                                    }
                                    else{
                                        Toast.makeText(context,"problem", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }){
                                Text("Continue")
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
fun prev(){
    WelcomeScreen()
}
