package com.example.timesphere.viewmodels

import FirebaseRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.timesphere.model.Shift

class ShiftsViewModel : ViewModel(){
    var shifts by mutableStateOf(emptyList<Shift>())
    private var firebaseRepository: FirebaseRepository = FirebaseRepository()
    var selectedShiftIndex by mutableStateOf(shifts.firstOrNull())




    fun getShiftsForMonth(monthNumber: Int? = null){
        firebaseRepository.getShiftsForMonth(monthNumber){
            shifts = it
        }
    }


}