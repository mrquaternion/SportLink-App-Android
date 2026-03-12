package com.mlr.sportlink.authentication.viewmodels

import com.mlr.sportlink.app.views.AuthenticationState

class GoogleAndAppleAuthVM {
    suspend fun signInGoogle(signupVM: SignupVM? = null): AuthenticationState {
        val result = AuthenticationManager.shared.signInWithGoogle()
        if (result == AuthenticationState.ONBOARDING_PENDING && signupVM != null) {
            val authUser = AuthenticationManager.shared.getAuthenticatedUserResult()
            signupVM.configureGoogleOnboarding(fallbackEmail = authUser.email)
        }
        return result
    }

    suspend fun signInApple(): AuthenticationState = AuthenticationState.UNAUTHENTICATED
}
