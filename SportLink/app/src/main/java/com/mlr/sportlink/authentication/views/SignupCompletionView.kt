package com.mlr.sportlink.authentication.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.mlr.sportlink.authentication.viewmodels.SignupVM
import com.mlr.sportlink.authentication.views.components.RoundedFilledButton
import com.mlr.sportlink.ui.theme.SportLinkTheme
import kotlinx.coroutines.launch

@Composable
fun SignupCompletionView(
    vm: SignupVM,
    onComplete: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val name = vm.firstname.ifBlank { SignupVM.derivedFirstname(vm.email.ifBlank { "player@sportlink.app" }) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Text(
            text = "Registration complete",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Welcome to SportLink, $name.",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.76f),
        )
        Text(
            text = "Your profile is ready to be saved into the Android flow.",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f),
        )

        RoundedFilledButton(
            action = {
                scope.launch {
                    vm.saveProfile()
                    onComplete()
                }
            },
        ) {
            Text("Get started")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignupCompletionViewPreview() {
    SportLinkTheme(darkTheme = false, dynamicColor = false) {
        SignupCompletionView(
            vm = SignupVM().apply {
                firstname = "Mathias"
                email = "mathias@example.com"
            },
            onComplete = {},
        )
    }
}
