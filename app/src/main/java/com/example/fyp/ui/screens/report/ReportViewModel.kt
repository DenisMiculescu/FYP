package com.example.fyp.ui.screens.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fyp.data.ReceiptModel
import com.example.fyp.data.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject
constructor(private val repository: RoomRepository) : ViewModel() {
    private val _receipts
            = MutableStateFlow<List<ReceiptModel>>(emptyList())
    val uiReceipts: StateFlow<List<ReceiptModel>>
            = _receipts.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAll().collect { listOfReceipts ->
                _receipts.value = listOfReceipts
            }
        }
    }
}
