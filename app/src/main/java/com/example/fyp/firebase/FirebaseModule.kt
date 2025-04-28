package com.example.fyp.firebase

import com.example.fyp.firebase.auth.AuthRepository
import com.example.fyp.firebase.services.AuthService
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun provideAuthRepository(auth: FirebaseAuth):
            AuthService = AuthRepository(firebaseAuth = auth)

}
