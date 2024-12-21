package com.example.timesphere.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timesphere.model.Shift
import com.example.timesphere.model.Utils
import com.example.timesphere.ui.theme.MonthSlider
import com.example.timesphere.viewmodels.AppViewModel
import com.example.timesphere.viewmodels.ShiftsViewModel
import java.util.Calendar




@Composable
fun ReportScreen(
    appViewModel: AppViewModel,
    shiftsViewModel: ShiftsViewModel = viewModel()
) {
    val TAG = "Report Screen"
    var currentMonthIndexFromSystem by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.MONTH)) }

    LaunchedEffect(Unit) {
        shiftsViewModel.getShiftsForMonth(currentMonthIndexFromSystem)
    }
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MonthSlider(modifier = Modifier.fillMaxWidth(),startMonthIndex = currentMonthIndexFromSystem,
            onClickNext = {
                currentMonthIndexFromSystem = (currentMonthIndexFromSystem + 1) % 12
                Log.d(TAG, "ReportScreen: current Month: $currentMonthIndexFromSystem")
                shiftsViewModel.getShiftsForMonth(currentMonthIndexFromSystem)
            }, onClickPrev = {
                currentMonthIndexFromSystem = (currentMonthIndexFromSystem - 1 + 12) % 12
                Log.d(TAG, "ReportScreen: current Month: $currentMonthIndexFromSystem")
                shiftsViewModel.getShiftsForMonth(currentMonthIndexFromSystem)
            })
        HorizontalDivider()
        ShiftTable(shiftsViewModel.shifts,appViewModel= appViewModel,shiftsViewModel = shiftsViewModel)
    }
}

@Composable
fun SummaryRow(
               appViewModel: AppViewModel,
               shiftsViewModel: ShiftsViewModel){
    var totalMoneyMade by remember { mutableDoubleStateOf(0.00) }
    var daysWorked by remember { mutableIntStateOf(0) }
    val utils = Utils()
    val rate = appViewModel.user.hourlyPay
    LaunchedEffect(shiftsViewModel.shifts) {
        totalMoneyMade = shiftsViewModel.shifts.sumOf { shift ->
            utils.calculateMoneyMadeInShift(shift, rate)
        }
        daysWorked = shiftsViewModel.shifts.size
    }
    Row(modifier = Modifier.fillMaxWidth()){
        Spacer(modifier= Modifier.weight(1f))
        Text(text = "Days worked:    $daysWorked",modifier = Modifier, style = TextStyle(
            fontSize = 16.sp,
        ))
        Spacer(modifier= Modifier.weight(1f))
        Text(text = "Money made:    ${String.format("%.2f",totalMoneyMade) }",modifier = Modifier, style = TextStyle(
            fontSize = 16.sp,
            color = Color.Green
        ))
        Spacer(modifier= Modifier.weight(1f))
    }

}

@Composable
fun ShiftTable(shifts: List<Shift>,
               appViewModel: AppViewModel,
               shiftsViewModel: ShiftsViewModel) {
    var showTimePicker by remember { mutableStateOf(false) }
    var isStartTime by remember { mutableStateOf(false) }
    if (showTimePicker && shiftsViewModel.selectedShiftIndex!=null) {
        if(isStartTime) {
            EditTimeBox(shiftsViewModel.selectedShiftIndex!!.getStartTime(), onValueChange = {
                shiftsViewModel.selectedShiftIndex!!.updateTimestamp(it,false) {
                    showTimePicker = false
                }
            }, onDismissRequest = {
                showTimePicker = false
            })
        }
        else {
            EditTimeBox(shiftsViewModel.selectedShiftIndex!!.getEndTime(), onValueChange = {
                shiftsViewModel.selectedShiftIndex!!.updateTimestamp(it,true) {
                    showTimePicker = false
                }
            }, onDismissRequest = {
                showTimePicker = false
            })
        }
    }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Table Header
            Row(Modifier.fillMaxWidth()) {
                Text(text = "Day", Modifier.weight(1f), textAlign = TextAlign.Center)
                Text(text = "Date", Modifier.weight(1f), textAlign = TextAlign.Center)
                Text(text = "Start Time", Modifier.weight(1f), textAlign = TextAlign.Center)
                Text(text = "End Time", Modifier.weight(1f), textAlign = TextAlign.Center)
                Text(text = "Total Time", Modifier.weight(1f), textAlign = TextAlign.Center)
                Text(text = "Money Made", Modifier.weight(1f), textAlign = TextAlign.Center)
            }
            HorizontalDivider(thickness = 1.dp, modifier = Modifier.fillMaxWidth())


            // Table Rows
            shifts.forEachIndexed{ index, shift ->
                val moneyMade = if (Utils().calculateMoneyMadeInShift(
                        shift,
                        appViewModel.user.hourlyPay
                    ) != 0.00
                ) {
                    String.format(
                        "%.2f",
                        Utils().calculateMoneyMadeInShift(shift, appViewModel.user.hourlyPay)
                    )
                } else {
                    "--"
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(IntrinsicSize.Min)
                ) {
                    Text(text = shift.getDay(), Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text(text = shift.date, Modifier.weight(1f), textAlign = TextAlign.Center)
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = shift.getStartTime(),
                        Modifier
                            .weight(1f)
                            .clickable {
                                if (shift.canModify()) {
                                    shiftsViewModel.selectedShiftIndex = shift
                                    showTimePicker = true
                                    isStartTime = true
                                }
                            },
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = shift.getEndTime(),
                        Modifier
                            .weight(1f)
                            .clickable {
                                if (shift.canModify()) {
                                    shiftsViewModel.selectedShiftIndex = shift
                                    showTimePicker = true
                                    isStartTime = false
                                }
                            },
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = shift.hoursWorked,
                        Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(text = moneyMade, Modifier.weight(1f), textAlign = TextAlign.Center)
                }
                HorizontalDivider(thickness = 1.dp, modifier = Modifier.fillMaxWidth())
            }
            SummaryRow(appViewModel = appViewModel,shiftsViewModel = shiftsViewModel)
        }

}





@Composable
fun TimePickerDialog(
    initialTime: String,
    onTimeSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var hours by remember { mutableStateOf(initialTime.split(":")[0]) }
    var minutes by remember { mutableStateOf(initialTime.split(":")[1]) }

    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Time Scroller
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Hours Selector
                TimeSelector(
                    value = hours.toInt(),
                    range = 0..23,
                    onValueChange = { hours = it }
                )
                Text(":")
                // Minutes Selector
                TimeSelector(
                    value = minutes.toInt(),
                    range = 0..59,
                    onValueChange = { minutes = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onDismissRequest) {
                    Text("Cancel")
                }
                Button(onClick = { onTimeSelected("${hours.padStart(2, '0')}:${minutes.padStart(2, '0')}") }) {
                    Text("OK")
                }
            }

        }
    }
}

@Composable
fun TimeSelector(value: Int, range: IntRange, onValueChange: (String) -> Unit) {
    var currentValue by remember { mutableStateOf(value) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Increment Button
        IconButton(onClick = {
            if (currentValue < range.last) currentValue++
            onValueChange(currentValue.toString())
        }) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
        }
        // Display Value
        Text(
            text = currentValue.toString().padStart(2, '0'),
            style = MaterialTheme.typography.titleLarge
        )
        // Decrement Button
        IconButton(onClick = {
            if (currentValue > range.first) currentValue--
            onValueChange(currentValue.toString())
        }) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
        }
    }
}

@Composable
fun EditTimeBox(time: String,onValueChange: (String) -> Unit,onDismissRequest: () -> Unit){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        TimePickerDialog(
            initialTime = time,
            onTimeSelected = { selectedTime ->
                onValueChange(selectedTime)
            },
            onDismissRequest = {
                onDismissRequest()
            }
        )
    }
}