package com.mlr.sportlink.authentication.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mlr.sportlink.app.views.AuthenticationState

class SigninVM {
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)

    suspend fun signInEmail(): AuthenticationState {
        if (!verifyTextFields()) {
            return AuthenticationState.UNAUTHENTICATED
        }

        return try {
            AuthenticationManager.shared.loginUser(email = email, password = password)
            AuthenticationManager.shared.checkAuthenticationState()
        } catch (_: IllegalStateException) {
            val error = "Incorrect username or password. Please check your login."
            emailError = error
            passwordError = error
            AuthenticationState.UNAUTHENTICATED
        }
    }

    fun resetFields() {
        email = ""
        password = ""
        emailError = null
        passwordError = null
    }

    suspend fun verifyEmailForResetPassword(): Boolean {
        if (email.isBlank()) {
            emailError = "Please enter a valid email."
            return false
        }

        return try {
            val exists = AuthenticationManager.shared.doesEmailMethodExist(email)
            if (!exists) {
                emailError = "No account is associated with this email."
                false
            } else {
                AuthenticationManager.shared.resetPassword(email)
                true
            }
        } catch (_: IllegalStateException) {
            emailError = "Unable to reset password right now."
            false
        }
    }

    private fun verifyTextFields(): Boolean {
        emailError = null
        passwordError = null

        if (email.isBlank()) {
            emailError = "Please enter a valid email."
            return false
        }

        if (!email.contains("@") || !email.contains(".")) {
            emailError = "Please enter a valid email address."
            return false
        }

        if (password.isBlank()) {
            passwordError = "Please enter your password."
            return false
        }

        return true
    }
}
