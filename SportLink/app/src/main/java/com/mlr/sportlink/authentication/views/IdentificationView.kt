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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mlr.sportlink.authentication.viewmodels.SignupVM
import com.mlr.sportlink.authentication.views.components.RoundedFilledButton
import com.mlr.sportlink.ui.theme.SportLinkTheme
import kotlinx.coroutines.launch

enum class SignUpStep {
    IDENTIFICATION,
    FAVOURITE_SPORTS,
    AVAILABILITIES,
    PROFILE_PICTURE,
    COMPLETION,
}

@Composable
fun IdentificationView(
    vm: SignupVM,
    initialStep: SignUpStep = SignUpStep.IDENTIFICATION,
    onCancel: () -> Unit,
    onComplete: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var currentStep by rememberSaveable(initialStep) { mutableStateOf(initialStep) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stepTitle(currentStep),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f),
            )
            TextButton(
                onClick = {
                    scope.launch {
                        vm.deleteUncompleteOnboardingUser()
                        onCancel()
                    }
                },
            ) {
                Text("Close")
            }
        }

        when (currentStep) {
            SignUpStep.IDENTIFICATION -> IdentificationStepView(
                vm = vm,
                onContinue = { currentStep = SignUpStep.FAVOURITE_SPORTS },
            )
            SignUpStep.FAVOURITE_SPORTS -> SportsSelectionView(
                vm = vm,
                showsBackButton = currentStep != initialStep,
                onBack = { currentStep = SignUpStep.IDENTIFICATION },
                onContinue = { currentStep = SignUpStep.AVAILABILITIES },
            )
            SignUpStep.AVAILABILITIES -> AvailabilitySelectionView(
                vm = vm,
                onBack = { currentStep = SignUpStep.FAVOURITE_SPORTS },
                onContinue = { currentStep = SignUpStep.PROFILE_PICTURE },
            )
            SignUpStep.PROFILE_PICTURE -> ProfilePictureView(
                vm = vm,
                onBack = { currentStep = SignUpStep.AVAILABILITIES },
                onContinue = { currentStep = SignUpStep.COMPLETION },
            )
            SignUpStep.COMPLETION -> SignupCompletionView(
                vm = vm,
                onComplete = onComplete,
            )
        }
    }
}

@Composable
fun IdentificationStepView(
    vm: SignupVM,
    onContinue: () -> Unit,
) {
    val isTappable = vm.firstname.trim().isNotEmpty() && vm.lastname.trim().isNotEmpty()

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "What's your name?",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "We use your name to personalize your profile and bookings.",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
        )
        OutlinedTextField(
            value = vm.firstname,
            onValueChange = { vm.firstname = it.toNameCase() },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("First name") },
            singleLine = true,
        )
        OutlinedTextField(
            value = vm.lastname,
            onValueChange = { vm.lastname = it.toNameCase() },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Last name") },
            singleLine = true,
        )
        RoundedFilledButton(
            isTappable = isTappable,
            action = onContinue,
        ) {
            Text("Continue")
        }
    }
}

private fun stepTitle(step: SignUpStep): String =
    when (step) {
        SignUpStep.IDENTIFICATION -> "Your name"
        SignUpStep.FAVOURITE_SPORTS -> "Favourite sports"
        SignUpStep.AVAILABILITIES -> "Availabilities"
        SignUpStep.PROFILE_PICTURE -> "Profile picture"
        SignUpStep.COMPLETION -> "You're ready"
    }

@Preview(showBackground = true)
@Composable
private fun IdentificationViewPreview() {
    SportLinkTheme(darkTheme = false, dynamicColor = false) {
        IdentificationView(
            vm = remember { SignupVM() },
            onCancel = {},
            onComplete = {},
        )
    }
}
