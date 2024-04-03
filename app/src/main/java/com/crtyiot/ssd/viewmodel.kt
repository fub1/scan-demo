package com.crtyiot.ssd.com.crtyiot.ssd

import android.text.Spannable.Factory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.math.log

class MainViewModel : ViewModel() {
    private val _scanState : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val scanstate : StateFlow<Boolean> = _scanState
    private val _inputData = MutableStateFlow("")
    val inputData : StateFlow<String> = _inputData
    private val _scanData = MutableStateFlow("")
    val scanData : StateFlow<String> = _scanData
    private val _scanError : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val scanError : StateFlow<Boolean> = _scanError

    fun changex() {
        _scanState.value = !(_scanState.value)
    }

    fun addInputData(data: String) {
        _inputData.value = data
    }

    fun addScanData(data: String) {
        _scanError.value = !(_scanError.value)
        _scanData.value = data
        Log.d("MainViewModel", "addScanData: $data${scanError.value}")
    }



    companion object {


    }
}