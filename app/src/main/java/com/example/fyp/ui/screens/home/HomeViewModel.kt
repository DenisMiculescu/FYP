package com.example.fyp.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fyp.firebase.services.AuthService
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authService: AuthService,
) : ViewModel() {

    var name = mutableStateOf("")
    var email = mutableStateOf("")
    val currentUser: FirebaseUser?
        get() = authService.currentUser

    init {
        viewModelScope.launch {
            authService.currentUser?.reload()
            name.value = authService.currentUser?.displayName.orEmpty()
            email.value = authService.currentUser?.email.orEmpty()
        }
    }

    fun isAuthenticated() = authService.isUserAuthenticatedInFirebase

}