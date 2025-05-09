package com.example.fyp.ui.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fyp.firebase.services.AuthService
import com.example.fyp.firebase.services.FirestoreService
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authService: AuthService,
    private val firestoreService: FirestoreService,
) : ViewModel() {

    val displayName get() = authService.currentUser?.displayName.toString()
    val photoUri get() = authService.customPhotoUri
    val email get() = authService.email.toString()

    fun signOut() {
        viewModelScope.launch { authService.signOut() }
    }

    fun updatePhotoUri(uri: Uri) {
        viewModelScope.launch {
            authService.updatePhoto(uri)
            firestoreService.updatePhotoUris(email, uri)
            Timber.i("Photo URI updated: $uri")
        }
    }
}