package com.mlr.sportlink.authentication.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.mlr.sportlink.authentication.viewmodels.SigninVM
import com.mlr.sportlink.authentication.viewmodels.SignupVM
import com.mlr.sportlink.authentication.views.components.RoundedFilledButton
import com.mlr.sportlink.authentication.views.components.RoundedStrokeButton
import com.mlr.sportlink.ui.theme.SportLinkTheme
import kotlinx.coroutines.launch

enum class AuthRoute {
    SIGNIN,
    SIGNUP,
}

@Composable
fun SigninView(
    signinVM: SigninVM,
    signupVM: SignupVM,
    onNavigateToSignup: () -> Unit,
    onStateChange: (AuthenticationState) -> Unit,
) {
    val googleVM = remember { GoogleAndAppleAuthVM() }
    val scope = rememberCoroutineScope()

    var isPasswordVisible by remember { mutableStateOf(false) }
    var showResetPasswordMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "Welcome back",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "Sign in with email or continue with Google.",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = signinVM.email,
                onValueChange = {
                    signinVM.email = it
                    signinVM.emailError = null
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                singleLine = true,
                isError = signinVM.emailError != null,
            )
            ErrorText(signinVM.emailError)

            OutlinedTextField(
                value = signinVM.password,
                onValueChange = {
                    signinVM.password = it
                    signinVM.passwordError = null
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password") },
                singleLine = true,
                isError = signinVM.passwordError != null,
                visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                ErrorText(
                    message = signinVM.passwordError,
                    modifier = Modifier.weight(1f),
                )
                TextButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Text(if (isPasswordVisible) "Hide" else "Show")
                }
            }
            TextButton(
                onClick = {
                    scope.launch {
                        if (signinVM.verifyEmailForResetPassword()) {
                            showResetPasswordMessage = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Forgot password?")
            }
        }

        RoundedFilledButton(
            fill = MaterialTheme.colorScheme.primary,
            action = {
                scope.launch {
                    val newState = signinVM.signInEmail()
                    if (newState != AuthenticationState.UNAUTHENTICATED) {
                        onStateChange(newState)
                    }
                }
            },
        ) {
            Text("Sign in")
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
                    onStateChange(newState)
                }
            },
        ) {
            Text("Continue with Google")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Don't have an account?",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
            )
            TextButton(onClick = onNavigateToSignup) {
                Text("Register")
            }
        }
    }

    if (showResetPasswordMessage) {
        AlertDialog(
            onDismissRequest = { showResetPasswordMessage = false },
            title = { Text("Instructions sent") },
            text = {
                Text("We sent password reset instructions to ${signinVM.email}.")
            },
            confirmButton = {
                TextButton(onClick = { showResetPasswordMessage = false }) {
                    Text("OK")
                }
            },
        )
    }
}

@Composable
fun ErrorText(
    message: String?,
    modifier: Modifier = Modifier,
) {
    Text(
        text = message ?: " ",
        modifier = modifier,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
    )
}

@Preview(showBackground = true)
@Composable
private fun SigninViewPreview() {
    SportLinkTheme(darkTheme = false, dynamicColor = false) {
        SigninView(
            signinVM = remember { SigninVM() },
            signupVM = remember { SignupVM() },
            onNavigateToSignup = {},
            onStateChange = {},
        )
    }
}
