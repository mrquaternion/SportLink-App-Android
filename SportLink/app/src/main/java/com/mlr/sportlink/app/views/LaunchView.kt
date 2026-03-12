package com.mlr.sportlink.app.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import com.mlr.sportlink.authentication.viewmodels.AuthenticationManager
import com.mlr.sportlink.authentication.viewmodels.SigninVM
import com.mlr.sportlink.authentication.viewmodels.SignupVM
import com.mlr.sportlink.authentication.views.IdentificationView
import com.mlr.sportlink.authentication.views.WelcomingView
import com.mlr.sportlink.ui.theme.SportLinkTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class AuthenticationState {
    LOADING,
    UNAUTHENTICATED,
    AUTHENTICATED,
    ONBOARDING_PENDING,
}

@Composable
fun LaunchView() {
    var authState by rememberSaveable { mutableStateOf(AuthenticationState.LOADING) }
    var hasSeenWelcoming by rememberSaveable { mutableStateOf(false) }
    val signinVM = remember { SigninVM() }
    val signupVM = remember { SignupVM() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(650)
        authState = AuthenticationManager.shared.checkAuthenticationState()
    }

    when (authState) {
        AuthenticationState.LOADING -> SplashScreen()
        AuthenticationState.AUTHENTICATED -> MainView(
            onLogout = {
                scope.launch {
                    AuthenticationManager.shared.logout()
                    signinVM.resetFields()
                    signupVM.resetAll()
                    authState = AuthenticationState.UNAUTHENTICATED
                }
            },
        )
        AuthenticationState.UNAUTHENTICATED -> WelcomingView(
            signinVM = signinVM,
            signupVM = signupVM,
            hasSeenWelcoming = hasSeenWelcoming,
            onHasSeenWelcomingChange = { hasSeenWelcoming = it },
            onStateChange = { state ->
                authState = state
            },
        )
        AuthenticationState.ONBOARDING_PENDING -> IdentificationView(
            vm = signupVM,
            initialStep = signupVM.onboardingStartStep,
            onCancel = {
                signupVM.resetAll()
                authState = AuthenticationState.UNAUTHENTICATED
            },
            onComplete = {
                authState = AuthenticationState.AUTHENTICATED
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LaunchViewPreview() {
    SportLinkTheme(dynamicColor = false) {
        LaunchView()
    }
}
