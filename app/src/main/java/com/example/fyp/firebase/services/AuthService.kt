package com.example.fyp.firebase.services

import com.example.fyp.firebase.auth.Response
import com.google.firebase.auth.FirebaseUser

typealias FirebaseSignInResponse = Response<FirebaseUser>

interface AuthService {
    val currentUserId: String
    val currentUser: FirebaseUser?
    val isUserAuthenticatedInFirebase: Boolean

    suspend fun authenticateUser(email: String, password: String)
            : FirebaseSignInResponse
    suspend fun createUser(name: String, email: String, password: String)
            : FirebaseSignInResponse
    suspend fun signOut()

}
