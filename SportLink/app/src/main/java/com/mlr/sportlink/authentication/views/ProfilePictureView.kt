package com.mlr.sportlink.authentication.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mlr.sportlink.authentication.viewmodels.SignupVM
import com.mlr.sportlink.authentication.views.components.RoundedFilledButton
import com.mlr.sportlink.ui.theme.SportLinkTheme

@Composable
fun ProfilePictureView(
    vm: SignupVM,
    onBack: () -> Unit = {},
    onContinue: () -> Unit,
) {
    val initials = buildString {
        vm.firstname.firstOrNull()?.let(::append)
        vm.lastname.firstOrNull()?.let(::append)
    }.ifBlank { "SL" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }

        Text(
            text = "Add a profile picture",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Image picking is still pending on Android, so this step uses an initials avatar for now.",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Surface(
                modifier = Modifier.size(164.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = initials.uppercase(),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }

        RoundedFilledButton(
            action = {
                vm.pictureToken = "initials-avatar"
                onContinue()
            },
        ) {
            Text("Continue")
        }

        TextButton(
            onClick = {
                vm.pictureToken = null
                onContinue()
            },
        ) {
            Text("Skip for now")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfilePictureViewPreview() {
    SportLinkTheme(darkTheme = false, dynamicColor = false) {
        ProfilePictureView(
            vm = SignupVM().apply {
                firstname = "Mathias"
                lastname = "Larochelle"
            },
            onContinue = {},
        )
    }
}
