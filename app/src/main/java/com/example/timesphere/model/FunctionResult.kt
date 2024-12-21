package com.example.timesphere.model

sealed class FunctionResult<out T> {
    object Loading : FunctionResult<Nothing>()
    object None : FunctionResult<Nothing>()
    data class Success<out T>(val data: T) : FunctionResult<T>()
    data class Error(val exception: Exception) : FunctionResult<Nothing>()
}