package com.mlr.sportlink.authentication.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mlr.sportlink.app.views.AuthenticationState
import com.mlr.sportlink.authentication.viewmodels.SigninVM
import com.mlr.sportlink.authentication.viewmodels.SignupVM
import com.mlr.sportlink.authentication.views.components.RoundedFilledButton
import com.mlr.sportlink.ui.theme.SportLinkTheme

@Composable
fun WelcomingView(
    signinVM: SigninVM,
    signupVM: SignupVM,
    hasSeenWelcoming: Boolean,
    onHasSeenWelcomingChange: (Boolean) -> Unit,
    onStateChange: (AuthenticationState) -> Unit,
) {
    var route by rememberSaveable {
        mutableStateOf(if (hasSeenWelcoming) AuthRoute.SIGNIN else null)
    }

    when (route) {
        null -> WelcomeLanding(
            onContinue = {
                onHasSeenWelcomingChange(true)
                route = AuthRoute.SIGNIN
            },
        )
        AuthRoute.SIGNIN -> SigninView(
            signinVM = signinVM,
            signupVM = signupVM,
            onNavigateToSignup = {
                signupVM.resetErrors()
                route = AuthRoute.SIGNUP
            },
            onStateChange = { state ->
                onHasSeenWelcomingChange(true)
                onStateChange(state)
            },
        )
        AuthRoute.SIGNUP -> SignupView(
            signupVM = signupVM,
            onNavigateToSignin = { route = AuthRoute.SIGNIN },
            onStateChange = { state ->
                onHasSeenWelcomingChange(true)
                onStateChange(state)
            },
            onInscriptionComplete = {
                onHasSeenWelcomingChange(true)
                onStateChange(AuthenticationState.ONBOARDING_PENDING)
            },
        )
    }
}

@Composable
private fun WelcomeLanding(
    onContinue: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = "Welcome to SportLink",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Discover new sports together and build your player profile.",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
            )
            RoundedFilledButton(
                modifier = Modifier.fillMaxWidth(),
                action = onContinue,
            ) {
                Text("Continue")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomingViewPreview() {
    SportLinkTheme(darkTheme = false, dynamicColor = false) {
        WelcomingView(
            signinVM = SigninVM(),
            signupVM = SignupVM(),
            hasSeenWelcoming = false,
            onHasSeenWelcomingChange = {},
            onStateChange = {},
        )
    }
}
