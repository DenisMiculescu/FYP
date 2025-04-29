package com.example.fyp.firebase.auth

import android.net.Uri
import com.example.fyp.firebase.services.AuthService
import com.example.fyp.firebase.services.FirebaseSignInResponse
import com.example.fyp.firebase.services.SignInWithGoogleResponse
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository
@Inject constructor(private val firebaseAuth: FirebaseAuth)
    : AuthService {

    override val currentUserId: String
        get() = firebaseAuth.currentUser?.uid.orEmpty()

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override val isUserAuthenticatedInFirebase : Boolean
        get() = firebaseAuth.currentUser != null

    override val email: String?
        get() = firebaseAuth.currentUser?.email

    override val customPhotoUri: Uri?
        get() = firebaseAuth.currentUser!!.photoUrl

    override suspend fun authenticateUser(email: String, password: String): FirebaseSignInResponse {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Response.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

    override suspend fun createUser(name: String, email: String, password: String): FirebaseSignInResponse {
        return try {
            val uri = Uri.parse("android.resource://com.example.fyp/drawable/default_user")
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.updateProfile(UserProfileChangeRequest
                .Builder()
                .setDisplayName(name)
                .setPhotoUri(uri)
                .build())?.await()
            return Response.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun authenticateGoogleUser(googleIdToken: String) : FirebaseSignInResponse {
        return try {
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            val result = firebaseAuth.signInWithCredential(firebaseCredential).await()
            Response.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

    override suspend fun firebaseSignInWithGoogle(
        googleCredential: AuthCredential
    ): SignInWithGoogleResponse {
        return try {
            val authResult = firebaseAuth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                //   addUserToFirestore()
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun updatePhoto(uri: Uri) : FirebaseSignInResponse {
        return try {
            currentUser!!.updateProfile(UserProfileChangeRequest
                .Builder()
                .setPhotoUri(uri)
                .build()).await()
            return Response.Success(currentUser!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

}
