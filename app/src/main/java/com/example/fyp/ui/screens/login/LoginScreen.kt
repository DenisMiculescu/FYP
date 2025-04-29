package com.example.fyp.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fyp.R
import com.example.fyp.firebase.auth.Response
import com.example.fyp.navigation.Home
import com.example.fyp.navigation.Login
import com.example.fyp.ui.components.general.ButtonComponent
import com.example.fyp.ui.components.general.GoogleSignInButtonComponent
import com.example.fyp.ui.components.general.HeadingLogoComponent
import com.example.fyp.ui.components.general.HeadingTextComponent
import com.example.fyp.ui.components.general.MyTextFieldComponent
import com.example.fyp.ui.components.general.PasswordTextFieldComponent
import com.example.fyp.ui.components.general.ShowLoader
import com.example.fyp.ui.components.general.UnderLinedTextComponent


@Composable
fun LoginScreen(
    onLogin: () -> Unit = {},
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()) {

    var isEnabled by remember { mutableStateOf(false) }
    val loginFlow = loginViewModel.loginFlow.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                HeadingTextComponent(value = stringResource(id = R.string.welcome))
                Spacer(modifier = Modifier.height(20.dp))
                HeadingLogoComponent()
                Spacer(modifier = Modifier.height(20.dp))

                MyTextFieldComponent(labelValue = stringResource(id = R.string.email),
                    painterResource(id = R.drawable.message),
                    onTextChanged = {
                        loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                    },
                    errorStatus = loginViewModel.loginUIState.value.emailError
                )

                PasswordTextFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    painterResource(id = R.drawable.lock),
                    onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                    },
                    errorStatus = loginViewModel.loginUIState.value.passwordError
                )

                Spacer(modifier = Modifier.height(20.dp))
                UnderLinedTextComponent(value = stringResource(id = R.string.forgot_password))

                Spacer(modifier = Modifier.height(30.dp))

                ButtonComponent(
                    value = stringResource(id = R.string.login),
                    onButtonClicked = {
                        loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                        onLogin()
                    },
                    isEnabled = loginViewModel.allValidationsPassed.value
                )
                isEnabled = loginViewModel.allValidationsPassed.value

                // Google Button here
                Spacer(modifier = Modifier.height(10.dp))
                val context = LocalContext.current
                GoogleSignInButtonComponent {
                    loginViewModel.signInWithGoogleCredentials(context)
                }
            }
        }
    }

    loginFlow.value?.let {
        when (it) {
            is Response.Failure -> {
                val context = LocalContext.current
                Toast.makeText(context, it.e.message, Toast.LENGTH_LONG).show()
                navController.popBackStack()
                navController.navigate(Login.route)
            }
            is Response.Loading -> {
                ShowLoader(message = "Please Wait...")
            }
            is Response.Success -> {
                LaunchedEffect(Unit) {
                    navController.navigate(Home.route) {
                        popUpTo(Login.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}

