package com.mlr.sportlink.authentication.viewmodels

import com.mlr.sportlink.app.views.AuthenticationState
import java.util.UUID

data class AuthDataResultModel(
    val uid: String,
    val email: String?,
    val photoUrl: String? = null,
)

enum class AuthProviderOption(val providerId: String) {
    EMAIL("password"),
    GOOGLE("google.com"),
    APPLE("apple.com"),
}

enum class OnboardingCancellationResult {
    ACCOUNT_DELETED,
    SIGNED_OUT_NEEDS_RECENT_LOGIN,
    SIGNED_OUT_DELETE_FAILED,
}

enum class DeleteAccountResult {
    DELETED,
    REQUIRES_RECENT_LOGIN,
    FAILED,
}

private data class StoredAuthUser(
    val uid: String,
    var email: String,
    var password: String,
    var provider: AuthProviderOption,
    var firstname: String = "",
    var lastname: String = "",
    var sports: List<String> = emptyList(),
    var disponibilities: Map<String, List<AvailabilityWindow>> = emptyMap(),
    var photoToken: String? = null,
    var hasSignedUpWithEmail: Boolean = false,
    var onboardingCompleted: Boolean = false,
)

class AuthenticationManager private constructor() {
    companion object {
        val shared = AuthenticationManager()

        private const val GOOGLE_DEMO_EMAIL = "google.demo@sportlink.app"
        private const val GOOGLE_DEMO_PASSWORD = "google-oauth-demo"
    }

    private val usersByUid = mutableMapOf<String, StoredAuthUser>()
    private val uidByEmail = mutableMapOf<String, String>()
    private var currentUserUid: String? = null

    fun getAuthenticatedUserResult(): AuthDataResultModel {
        val user = currentUser() ?: error("No authenticated user.")
        return user.toAuthDataResult()
    }

    suspend fun createUser(email: String, password: String): AuthDataResultModel =
        registerUser(email = email, password = password, provider = AuthProviderOption.EMAIL)

    suspend fun loginUser(email: String, password: String): AuthDataResultModel {
        val user = findUserByEmail(email) ?: error("Invalid credentials.")
        check(user.password == password) { "Invalid credentials." }
        currentUserUid = user.uid
        return user.toAuthDataResult()
    }

    suspend fun logout() {
        currentUserUid = null
    }

    fun getProviders(): List<AuthProviderOption> {
        val user = currentUser() ?: error("No authenticated user.")
        return listOf(user.provider)
    }

    suspend fun checkAuthenticationState(): AuthenticationState {
        val user = currentUser() ?: return AuthenticationState.UNAUTHENTICATED
        return if (user.onboardingCompleted) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.ONBOARDING_PENDING
        }
    }

    suspend fun isOnboardingCompleted(uid: String): Boolean =
        usersByUid[uid]?.onboardingCompleted == true

    suspend fun doesEmailMethodExist(email: String): Boolean =
        findUserByEmail(email) != null

    suspend fun resetPassword(email: String) {
        check(doesEmailMethodExist(email)) { "No account found for $email" }
    }

    suspend fun userExists(uid: String): Boolean = usersByUid.containsKey(uid)

    suspend fun createUserDoc(
        uid: String,
        firstname: String? = null,
        lastname: String? = null,
    ) {
        val user = usersByUid[uid] ?: error("Unknown user: $uid")
        user.firstname = firstname?.trim().orEmpty()
        user.lastname = lastname?.trim().orEmpty()
        user.onboardingCompleted = false
    }

    suspend fun saveProfile(
        uid: String,
        email: String,
        hasSignedUpWithEmail: Boolean,
        firstname: String,
        lastname: String,
        sports: List<String>,
        disponibilities: Map<String, List<AvailabilityWindow>>,
        photoToken: String?,
    ) {
        val user = usersByUid[uid] ?: error("Unknown user: $uid")
        val normalizedEmail = email.normalizeEmail()

        if (user.email != normalizedEmail) {
            uidByEmail.remove(user.email)
            uidByEmail[normalizedEmail] = uid
        }

        user.email = normalizedEmail
        user.hasSignedUpWithEmail = hasSignedUpWithEmail
        user.firstname = firstname.trim()
        user.lastname = lastname.trim()
        user.sports = sports
        user.disponibilities = disponibilities
        user.photoToken = photoToken
        user.onboardingCompleted = true
    }

    suspend fun deleteUserPermanently(): DeleteAccountResult {
        val user = currentUser() ?: return DeleteAccountResult.FAILED
        usersByUid.remove(user.uid)
        uidByEmail.remove(user.email)
        currentUserUid = null
        return DeleteAccountResult.DELETED
    }

    suspend fun cancelIncompleteOnboarding(): OnboardingCancellationResult {
        val user = currentUser() ?: return OnboardingCancellationResult.SIGNED_OUT_DELETE_FAILED

        if (user.onboardingCompleted) {
            currentUserUid = null
            return OnboardingCancellationResult.SIGNED_OUT_DELETE_FAILED
        }

        usersByUid.remove(user.uid)
        uidByEmail.remove(user.email)
        currentUserUid = null
        return OnboardingCancellationResult.ACCOUNT_DELETED
    }

    suspend fun signInWithGoogle(): AuthenticationState {
        val existingUser = findUserByEmail(GOOGLE_DEMO_EMAIL)
        if (existingUser != null) {
            currentUserUid = existingUser.uid
            return if (existingUser.onboardingCompleted) {
                AuthenticationState.AUTHENTICATED
            } else {
                AuthenticationState.ONBOARDING_PENDING
            }
        }

        val authUser = registerUser(
            email = GOOGLE_DEMO_EMAIL,
            password = GOOGLE_DEMO_PASSWORD,
            provider = AuthProviderOption.GOOGLE,
        )
        createUserDoc(uid = authUser.uid, firstname = "Google", lastname = "Player")
        currentUserUid = authUser.uid
        return AuthenticationState.ONBOARDING_PENDING
    }

    private fun registerUser(
        email: String,
        password: String,
        provider: AuthProviderOption,
    ): AuthDataResultModel {
        val normalizedEmail = email.normalizeEmail()
        check(normalizedEmail.isNotEmpty()) { "Email cannot be empty." }
        check(!uidByEmail.containsKey(normalizedEmail)) { "Account already exists." }

        val uid = UUID.randomUUID().toString()
        val user = StoredAuthUser(
            uid = uid,
            email = normalizedEmail,
            password = password,
            provider = provider,
            hasSignedUpWithEmail = provider == AuthProviderOption.EMAIL,
        )

        usersByUid[uid] = user
        uidByEmail[normalizedEmail] = uid
        currentUserUid = uid

        return user.toAuthDataResult()
    }

    private fun currentUser(): StoredAuthUser? = currentUserUid?.let(usersByUid::get)

    private fun findUserByEmail(email: String): StoredAuthUser? =
        uidByEmail[email.normalizeEmail()]?.let(usersByUid::get)
}

private fun StoredAuthUser.toAuthDataResult(): AuthDataResultModel =
    AuthDataResultModel(
        uid = uid,
        email = email,
        photoUrl = photoToken,
    )

private fun String.normalizeEmail(): String = trim().lowercase()
