package com.example.fyp.ui.screens.report

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fyp.data.ReceiptModel
import com.example.fyp.data.api.RetrofitRepository
import com.example.fyp.data.repository.RoomRepository
import com.example.fyp.firebase.services.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject
constructor(
    private val repository: RetrofitRepository,
    private val authService: AuthService
) : ViewModel() {
    private val _receipts
            = MutableStateFlow<List<ReceiptModel>>(emptyList())
    val uiReceipts: StateFlow<List<ReceiptModel>>
            = _receipts.asStateFlow()
    var isErr = mutableStateOf(false)
    var isLoading = mutableStateOf(false)
    var error = mutableStateOf(Exception())

//    init {
//        viewModelScope.launch {
//            repository.getAll().collect { listOfReceipts ->
//                _receipts.value = listOfReceipts
//            }
//        }
//    }

    init { getReceipts() }

    fun getReceipts() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                _receipts.value = repository.getAll(authService.email!!)
                isErr.value = false
                isLoading.value = false
                Timber.i("DVM RVM = : ${_receipts.value}")
            }
            catch(e:Exception) {
                isErr.value = true
                isLoading.value = false
                error.value = e
                Timber.i("RVM Error ${e.message}")
            }
        }
    }

    fun deleteReceipt(receipt: ReceiptModel) {
        viewModelScope.launch {
            repository.delete(authService.email!!, receipt)
        }
    }
}
