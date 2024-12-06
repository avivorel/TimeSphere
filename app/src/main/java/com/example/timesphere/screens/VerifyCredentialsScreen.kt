package com.example.timesphere.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.timesphere.ui.theme.RoundedCornerCardTop
import com.example.timesphere.viewmodels.AppViewModel

@Composable
fun VerifyCredentials(
    navHost: NavHostController = rememberNavController(),
    appViewModel : AppViewModel = viewModel()
){
    val headline by remember {mutableStateOf("Verify Credentials")}
    val headlineSubtext by remember { mutableStateOf(
        """Those were given to us by your Employer.
            |If you see anything wrong here,
            |please contact your Immediate supervisor.
        """.trimMargin()
    ) }
    var password by remember { mutableStateOf("Password") }
    var repeatPassword by remember { mutableStateOf("Repeat") }
    val context = LocalContext.current

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
                                value = appViewModel.user.firstName,
                                onValueChange = {
                                },
                                enabled = false
                            )
                            Spacer(modifier = Modifier.weight(0.5f))
                            TextField(modifier = Modifier.weight(1f),
                                textStyle = TextStyle(textAlign = TextAlign.Center),
                                value = appViewModel.user.lastName,
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
                                value = appViewModel.user.id,
                                onValueChange = {
                                },
                                enabled = false
                            )
                            Spacer(modifier = Modifier.weight(0.5f))
                            TextField(modifier = Modifier.weight(1f),
                                textStyle = TextStyle(textAlign = TextAlign.Center),
                                value = appViewModel.user.phoneNumber,
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
                                value = appViewModel.user.email,
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
                            TextField(modifier = Modifier
                                .focusable(true)
                                .onFocusChanged {
                                    if (it.isFocused && appViewModel.userPassword == "Password") {
                                        appViewModel.userPassword = ""
                                    }
                                }.weight(1f),
                                textStyle = TextStyle(textAlign = TextAlign.Center),
                                value = appViewModel.userPassword,
                                onValueChange = {
                                    appViewModel.userPassword = it
                                },
                                enabled = true
                            )
                            Spacer(modifier = Modifier.weight(0.5f))
                            TextField(modifier = Modifier
                                .focusable(true)
                                .onFocusChanged {
                                    if (it.isFocused && appViewModel.userPasswordRepeated == "Repeat password") {
                                        appViewModel.userPasswordRepeated = ""
                                    }
                                }.weight(1f),
                                textStyle = TextStyle(textAlign = TextAlign.Center),
                                value = appViewModel.userPasswordRepeated,
                                onValueChange = {
                                    appViewModel.userPasswordRepeated = it
                                },
                                enabled = true
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
                                value = appViewModel.user.hourlyPay.toString(),
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
                            Button(onClick = {
                                if(appViewModel.checkIfPasswordMatch()){
                                    appViewModel.isUserAlreadySignedUp { isSignedUp ->
                                        if (isSignedUp){
                                            Toast.makeText(context,"Already signed up",Toast.LENGTH_LONG).show()
                                        }
                                        else{
                                            appViewModel.signUpUser(){
                                                Toast.makeText(context,"Signed up successfully",Toast.LENGTH_LONG).show()
                                                navHost.navigate("home_screen")
                                            }
                                        }
                                    }
                                }
                            }){
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