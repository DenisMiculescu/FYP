package com.example.fyp.ui.screens.receipt

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fyp.data.ReceiptModel
import com.example.fyp.data.api.RetrofitRepository
import com.example.fyp.data.repository.RoomRepository
import com.example.fyp.firebase.services.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject
constructor(private val repository: RetrofitRepository,
    private val authService: AuthService) : ViewModel()
{
    var isErr = mutableStateOf(false)
    var error = mutableStateOf(Exception())
    var isLoading = mutableStateOf(false)

//    fun insert(receipts: ReceiptModel)
//            = viewModelScope.launch {
//        repository.insert(receipts)
//    }

    fun insert(receipt: ReceiptModel) =
        viewModelScope.launch {
            try {
                isLoading.value = true
                repository.insert(authService.email!!,receipt)
                isErr.value = false
                isLoading.value = false
            } catch (e: Exception) {
                isErr.value = true
                error.value = e
                isLoading.value = false
            }
            Timber.i("DVM Insert Message = : ${error.value.message} and isError ${isErr.value}")
        }
}
