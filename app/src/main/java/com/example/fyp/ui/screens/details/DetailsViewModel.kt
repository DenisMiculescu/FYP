package com.example.fyp.ui.screens.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fyp.data.models.ReceiptModel
import com.example.fyp.firebase.services.AuthService
import com.example.fyp.firebase.services.FirestoreService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject
constructor(private val repository: FirestoreService,
            private val authService: AuthService,
            savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var receipt = mutableStateOf(ReceiptModel())
    val id: String = checkNotNull(savedStateHandle["id"])
    var isErr = mutableStateOf(false)
    var error = mutableStateOf(Exception())
    var isLoading = mutableStateOf(false)

    init {
        viewModelScope.launch {
            try {
                isLoading.value = true
                receipt.value = repository.get(authService.email!!, id)!!
                isErr.value = false
                isLoading.value = false
            } catch (e: Exception) {
                isErr.value = true
                error.value = e
                isLoading.value = false
            }
        }
    }

    fun updateReceipt(receipt: ReceiptModel) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                repository.update(authService.email!!,receipt)
                isErr.value = false
                isLoading.value = false
            } catch (e: Exception) {
                isErr.value = true
                error.value = e
                isLoading.value = false
            }
        }
    }
}