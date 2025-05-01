package com.example.fyp.ui.screens.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fyp.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fyp.firebase.auth.Response
import com.example.fyp.navigation.Home
import com.example.fyp.navigation.Register
import com.example.fyp.ui.components.general.ButtonComponent
import com.example.fyp.ui.components.general.CheckboxComponent
import com.example.fyp.ui.components.general.ClickableLoginTextComponent
import com.example.fyp.ui.components.general.DividerTextComponent
import com.example.fyp.ui.components.general.HeadingLogoComponent
import com.example.fyp.ui.components.general.HeadingTextComponent
import com.example.fyp.ui.components.general.MyTextFieldComponent
import com.example.fyp.ui.components.general.NormalTextComponent
import com.example.fyp.ui.components.general.PasswordTextFieldComponent
import com.example.fyp.ui.components.general.ShowLoader

@Composable
fun RegisterScreen(
    onRegister: () -> Unit = {},
    navController: NavController,
    registerViewModel: RegisterViewModel = hiltViewModel()) {

    val registerFlow = registerViewModel.signupFlow.collectAsState()

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
            Column(modifier = Modifier.fillMaxSize()) {

                HeadingTextComponent(value = stringResource(id = R.string.create_account))
                Spacer(modifier = Modifier.height(20.dp))
                HeadingLogoComponent()
                Spacer(modifier = Modifier.height(20.dp))
                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.first_name),
                    painterResource(id = R.drawable.profile),
                    onTextChanged = {
                        registerViewModel.onEvent(RegisterUIEvent.FirstNameChanged(it))
                    },
                    errorStatus = registerViewModel.registrationUIState.value.firstNameError
                )
                MyTextFieldComponent(
                    labelValue = stringResource(id = R.string.email),
                    painterResource = painterResource(id = R.drawable.message),
                    onTextChanged = {
                        registerViewModel.onEvent(RegisterUIEvent.EmailChanged(it))
                    },
                    errorStatus = registerViewModel.registrationUIState.value.emailError
                )
                PasswordTextFieldComponent(
                    labelValue = stringResource(id = R.string.password),
                    painterResource = painterResource(id = R.drawable.lock),
                    onTextSelected = {
                        registerViewModel.onEvent(RegisterUIEvent.PasswordChanged(it))
                    },
                    errorStatus = registerViewModel.registrationUIState.value.passwordError
                )
                CheckboxComponent(value = stringResource(id = R.string.terms_and_conditions),
                    onTextSelected = {},
                    onCheckedChange = {
                        registerViewModel.onEvent(RegisterUIEvent.PrivacyPolicyCheckBoxClicked(it))
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))
                ButtonComponent(
                    value = stringResource(id = R.string.register),
                    onButtonClicked = {
                        registerViewModel.onEvent(RegisterUIEvent.RegisterButtonClicked)
                    },
                    isEnabled = registerViewModel.allValidationsPassed.value
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        if(registerViewModel.signUpInProgress.value) {
            CircularProgressIndicator()
        }
    }

    registerFlow.value?.let {
        when (it) {
            is Response.Failure -> {
                val context = LocalContext.current
                Toast.makeText(context, it.e.message, Toast.LENGTH_LONG).show()
                navController.popBackStack()
                navController.navigate(Register.route)
            }
            is Response.Loading -> {
                ShowLoader(message = "Please Wait...")
            }
            is Response.Success -> {
                LaunchedEffect(Unit) {
                    onRegister()
                    navController.navigate(Home.route) {
                        popUpTo(Register.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}
