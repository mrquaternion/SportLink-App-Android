package com.mlr.sportlink.authentication.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mlr.sportlink.authentication.views.SignUpStep

data class AvailabilityWindow(
    val startLabel: String,
    val endLabel: String,
)

class SignupVM {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var firstname by mutableStateOf("")
    var lastname by mutableStateOf("")
    var favouriteSports by mutableStateOf(emptyList<String>())
    var availabilities by mutableStateOf<Map<String, List<AvailabilityWindow>>>(emptyMap())
    var pictureToken by mutableStateOf<String?>(null)

    var emailError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var passwordConfirmError by mutableStateOf<String?>(null)

    var hasSignedUpWithEmail by mutableStateOf(false)
    var onboardingStartStep by mutableStateOf(SignUpStep.IDENTIFICATION)

    suspend fun signupFirebase(): Boolean {
        if (!verifyTextFields()) {
            return false
        }

        return try {
            val user = AuthenticationManager.shared.createUser(email = email, password = password)
            hasSignedUpWithEmail = true
            AuthenticationManager.shared.createUserDoc(uid = user.uid)
            true
        } catch (_: IllegalStateException) {
            emailError = "This email is already associated with an account."
            false
        }
    }

    suspend fun deleteUncompleteOnboardingUser(): OnboardingCancellationResult =
        AuthenticationManager.shared.cancelIncompleteOnboarding()

    suspend fun saveProfile() {
        val authUser = runCatching { AuthenticationManager.shared.getAuthenticatedUserResult() }.getOrNull()
            ?: return

        runCatching {
            AuthenticationManager.shared.saveProfile(
                uid = authUser.uid,
                email = email,
                hasSignedUpWithEmail = hasSignedUpWithEmail,
                firstname = firstname,
                lastname = lastname,
                sports = favouriteSports,
                disponibilities = availabilities,
                photoToken = pictureToken,
            )
        }
    }

    fun configureOnboardingStart(
        firstname: String? = null,
        lastname: String? = null,
    ) {
        val cleanedFirstname = firstname?.trim().orEmpty()
        val cleanedLastname = lastname?.trim().orEmpty()

        if (cleanedFirstname.isNotEmpty()) {
            this.firstname = cleanedFirstname.toNameCase()
        }

        if (cleanedLastname.isNotEmpty()) {
            this.lastname = cleanedLastname.toNameCase()
        }

        onboardingStartStep = if (this.firstname.isBlank() || this.lastname.isBlank()) {
            SignUpStep.IDENTIFICATION
        } else {
            SignUpStep.FAVOURITE_SPORTS
        }
    }

    fun configureGoogleOnboarding(fallbackEmail: String?) {
        val cleanedEmail = fallbackEmail?.trim().orEmpty()
        if (cleanedEmail.isNotEmpty()) {
            email = cleanedEmail
            firstname = derivedFirstname(cleanedEmail)
        }
        lastname = ""
        onboardingStartStep = SignUpStep.FAVOURITE_SPORTS
    }

    fun toggleFavouriteSport(sportName: String) {
        favouriteSports = if (favouriteSports.contains(sportName)) {
            favouriteSports - sportName
        } else {
            favouriteSports + sportName
        }
    }

    fun setAvailability(day: String, slots: List<AvailabilityWindow>) {
        val next = availabilities.toMutableMap()
        if (slots.isEmpty()) {
            next.remove(day)
        } else {
            next[day] = slots
        }
        availabilities = next.toMap()
    }

    fun addAvailabilityWindow(day: String, slot: AvailabilityWindow) {
        val existing = availabilities[day].orEmpty()
        setAvailability(day, existing + slot)
    }

    fun removeAvailabilityWindow(day: String, index: Int) {
        val existing = availabilities[day].orEmpty()
        if (index !in existing.indices) {
            return
        }
        setAvailability(day, existing.filterIndexed { currentIndex, _ -> currentIndex != index })
    }

    fun resetFields() {
        email = ""
        password = ""
        confirmPassword = ""
    }

    fun resetErrors() {
        emailError = null
        passwordError = null
        passwordConfirmError = null
    }

    fun resetAll() {
        resetFields()
        firstname = ""
        lastname = ""
        favouriteSports = emptyList()
        availabilities = emptyMap()
        pictureToken = null
        hasSignedUpWithEmail = false
        onboardingStartStep = SignUpStep.IDENTIFICATION
        resetErrors()
    }

    private fun verifyTextFields(): Boolean {
        resetErrors()
        val normalizedEmail = email.trim()

        if (normalizedEmail.isEmpty()) {
            emailError = "Please enter an email."
        } else if (!normalizedEmail.matches(EMAIL_REGEX)) {
            emailError = "Please enter a valid email address."
        } else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters."
        } else if (password.isBlank()) {
            passwordError = "Please enter a valid password."
        } else if (confirmPassword.isBlank()) {
            passwordConfirmError = "Please confirm your password."
        } else if (confirmPassword != password) {
            passwordConfirmError = "Confirmed password must match."
        }

        return emailError == null &&
            passwordError == null &&
            passwordConfirmError == null
    }

    companion object {
        private val EMAIL_REGEX =
            Regex("^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

        fun derivedFirstname(email: String): String {
            val localPart = email.substringBefore("@")
            val tokens = localPart
                .split(".", "-", "_", "+")
                .filter { it.isNotBlank() }

            return (tokens.firstOrNull() ?: localPart).toNameCase()
        }
    }
}

fun String.toNameCase(): String =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
