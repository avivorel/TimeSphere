package com.example.timesphere.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.timesphere.model.FunctionResult
import com.example.timesphere.ui.theme.RoundedCornerCardTop
import com.example.timesphere.viewmodels.AppViewModel


val TAG = "WelcomeScreen"


@Composable
fun WelcomeScreen(
    navHost :NavHostController = rememberNavController(),
    appViewModel : AppViewModel = viewModel()
){
    val headlineText by remember {mutableStateOf("Welcome to")}
    val appName by remember { mutableStateOf("TimeSphere") }
    val numberRegexPattern = remember { Regex("^\\d+\$") }
    var result by remember { mutableStateOf<FunctionResult<Map<*, *>?>>(FunctionResult.None) }
    val context = LocalContext.current
    var navigatedOut by remember {
        mutableStateOf(false)
    }


    when (result) {
        is FunctionResult.Loading -> {
            Toast.makeText(context,"Loading",Toast.LENGTH_LONG).show()
        }
        is FunctionResult.Success -> {
            val employeeData = (result as FunctionResult.Success<*>).data
            if (employeeData != null) {
                appViewModel.updateUserAll(employeeData)
                if(!navigatedOut) {
                    navHost.navigate("verify_credentials_screen")
                    navigatedOut = true
                }
            } else {
                Toast.makeText(context,"No employee found",Toast.LENGTH_LONG).show()
            }
        }
        is FunctionResult.Error -> {
            Toast.makeText(context,"Error",Toast.LENGTH_LONG).show()
        }
        else -> {
        }
    }

    Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    headlineText,
                    modifier = Modifier,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
            Row(
                modifier = Modifier
                    .weight(0.75f)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    appName,
                    modifier = Modifier,
                    fontSize = 35.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
            Row(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                RoundedCornerCardTop(
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(top = 30.dp, bottom = 50.dp)
                            ) {
                                Text(
                                    "Login",
                                    modifier = Modifier,
                                    fontSize = 35.sp,
                                    fontFamily = FontFamily.SansSerif
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(top = 20.dp, bottom = 20.dp)
                            ) {
                                TextField(
                                    label = { Text("Employer Id") },
                                    value = appViewModel.user.employerId,
                                    modifier = Modifier
                                        .focusable(true)
                                        .onFocusChanged {
                                            if (it.isFocused && appViewModel.user.employerId == "Employer ID") {
                                                appViewModel.updateUserField(
                                                    "employerId",
                                                    ""
                                                ) // Clear when focused
                                            }
                                        },
                                    onValueChange = {
                                        appViewModel.updateUserField("employerId",it)
                                        Log.d(TAG, "employerId value: ${appViewModel.user.employerId}")

                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(top = 20.dp, bottom = 20.dp)
                            ) {
                                TextField(
                                    label = { Text("Email") },
                                    value = appViewModel.user.email,
                                    modifier = Modifier
                                        .focusable(true)
                                        .onFocusChanged {
                                            if (it.isFocused && appViewModel.user.email == "example@gmail.com") {
                                                appViewModel.updateUserField(
                                                    "email",
                                                    ""
                                                ) // Clear when focused
                                            }
                                        },
                                    onValueChange = {
                                        appViewModel.updateUserField("email",it)
                                        Log.d(TAG, "email value: ${appViewModel.user.email}")
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(top = 20.dp, bottom = 20.dp)
                            ) {
                                TextField(
                                    label = { Text("תעודת זהות") },
                                    modifier = Modifier
                                        .focusable(true)
                                        .onFocusChanged {
                                            if (it.isFocused && appViewModel.user.id == "Israeli issued ID") {
                                                appViewModel.updateUserField("id", "")
                                            }
                                        },
                                    value = appViewModel.user.id,
                                    onValueChange = {
                                        appViewModel.updateUserField("id",it)
                                        Log.d(TAG, "userIdText value: ${appViewModel.user.id}")
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(top = 20.dp, bottom = 20.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(checked = false, onCheckedChange = {})
                                Text("Allow FaceID")
                            }
                            Row(
                                modifier = Modifier
                                    .padding(top = 20.dp, bottom = 20.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(onClick = {
                                    appViewModel.findEmployeeInEmployer(employerId = appViewModel.user.employerId, employeeId = appViewModel.user.id,
                                        email = appViewModel.user.email).observeForever { searchResult ->
                                        result = searchResult
                                    }
                                }) {
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
