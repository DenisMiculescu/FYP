package com.example.fyp.ui.screens.profile

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fyp.R
import com.example.fyp.ui.components.general.HeadingTextComponent
import com.example.fyp.ui.components.general.ShowPhotoPicker
import com.example.fyp.ui.screens.login.LoginViewModel
import com.example.fyp.ui.screens.register.RegisterViewModel

@Composable
fun ProfileScreen(
    onSignOut: () -> Unit = {},
    profileViewModel: ProfileViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    registerViewModel: RegisterViewModel = hiltViewModel()
) {

    var photoUri: Uri? by remember { mutableStateOf(profileViewModel.photoUri) }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        HeadingTextComponent(value = stringResource(id = R.string.account_settings))
        Spacer(modifier = Modifier.height(10.dp))

        val fallbackUri = Uri.parse("com.example.fyp:drawable/${R.drawable.default_user}")
        val uriToShow = if (photoUri?.toString().isNullOrEmpty()) fallbackUri else photoUri!!

        ProfileContent(
            photoUri = uriToShow,
            displayName = profileViewModel.displayName.ifEmpty { "User" },
            email = profileViewModel.email
        )

        ShowPhotoPicker(
            onPhotoUriChanged = {
                photoUri = it
                profileViewModel.updatePhotoUri(photoUri!!)
            }
        )
        Button(
            onClick = {
                profileViewModel.signOut()
                onSignOut()
                loginViewModel.resetLoginFlow()
                registerViewModel.resetRegisterFlow()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
        ) {
            Text(text = "Logout")
        }
    }}
