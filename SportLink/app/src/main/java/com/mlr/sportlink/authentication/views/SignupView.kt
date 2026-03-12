package com.mlr.sportlink.authentication.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mlr.sportlink.app.views.AuthenticationState
import com.mlr.sportlink.authentication.viewmodels.GoogleAndAppleAuthVM
import com.mlr.sportlink.authentication.viewmodels.SignupVM
import com.mlr.sportlink.authentication.views.components.RoundedFilledButton
import com.mlr.sportlink.authentication.views.components.RoundedStrokeButton
import com.mlr.sportlink.ui.theme.SportLinkTheme
import kotlinx.coroutines.launch

@Composable
fun SignupView(
    signupVM: SignupVM,
    onNavigateToSignin: () -> Unit,
    onStateChange: (AuthenticationState) -> Unit,
    onInscriptionComplete: () -> Unit,
) {
    val googleVM = remember { GoogleAndAppleAuthVM() }
    val scope = rememberCoroutineScope()

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            signupVM.resetErrors()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Text(
            text = "Get started",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "Create your account to start onboarding.",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
        )

        OutlinedTextField(
            value = signupVM.email,
            onValueChange = {
                signupVM.email = it
                signupVM.emailError = null
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            singleLine = true,
            isError = signupVM.emailError != null,
        )
        ErrorText(signupVM.emailError)

        OutlinedTextField(
            value = signupVM.password,
            onValueChange = {
                signupVM.password = it
                signupVM.passwordError = null
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            singleLine = true,
            isError = signupVM.passwordError != null,
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
        )
        PasswordControls(
            isVisible = isPasswordVisible,
            onToggle = { isPasswordVisible = !isPasswordVisible },
            error = signupVM.passwordError,
        )

        OutlinedTextField(
            value = signupVM.confirmPassword,
            onValueChange = {
                signupVM.confirmPassword = it
                signupVM.passwordConfirmError = null
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Confirm password") },
            singleLine = true,
            isError = signupVM.passwordConfirmError != null,
            visualTransformation = if (isConfirmPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
        )
        PasswordControls(
            isVisible = isConfirmPasswordVisible,
            onToggle = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
            error = signupVM.passwordConfirmError,
        )

        RoundedFilledButton(
            fill = MaterialTheme.colorScheme.primary,
            action = {
                scope.launch {
                    if (signupVM.signupFirebase()) {
                        onInscriptionComplete()
                    }
                }
            },
        ) {
            Text("Sign up")
        }

        Text(
            text = "or",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f),
        )

        RoundedStrokeButton(
            action = {
                scope.launch {
                    val newState = googleVM.signInGoogle(signupVM)
                    if (newState == AuthenticationState.ONBOARDING_PENDING) {
                        onInscriptionComplete()
                    } else {
                        onStateChange(newState)
                    }
                }
            },
        ) {
            Text("Sign up with Google")
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Already have an account?",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
            )
            TextButton(onClick = onNavigateToSignin) {
                Text("Login")
            }
        }
    }
}

@Composable
private fun PasswordControls(
    isVisible: Boolean,
    onToggle: () -> Unit,
    error: String?,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        ErrorText(
            message = error,
            modifier = Modifier.weight(1f),
        )
        TextButton(onClick = onToggle) {
            Text(if (isVisible) "Hide" else "Show")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignupViewPreview() {
    SportLinkTheme(darkTheme = false, dynamicColor = false) {
        SignupView(
            signupVM = remember { SignupVM() },
            onNavigateToSignin = {},
            onStateChange = {},
            onInscriptionComplete = {},
        )
    }
}
