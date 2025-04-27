package com.example.fyp.ui.screens.receipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fyp.data.ReceiptModel
import com.example.fyp.data.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject
constructor(private val repository: RoomRepository) : ViewModel() {

    fun insert(receipts: ReceiptModel)
            = viewModelScope.launch {
        repository.insert(receipts)
    }
}
