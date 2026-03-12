package com.mlr.sportlink.authentication.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mlr.sportlink.authentication.viewmodels.SignupVM
import com.mlr.sportlink.authentication.views.components.RoundedFilledButton
import com.mlr.sportlink.core.models.local.Sport
import com.mlr.sportlink.ui.theme.SportLinkTheme

@Composable
fun SportsSelectionView(
    vm: SignupVM,
    showsBackButton: Boolean = true,
    onBack: () -> Unit = {},
    onContinue: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        if (showsBackButton) {
            TextButton(onClick = onBack) {
                Text("Back")
            }
        }

        Text(
            text = "Your favourite sports",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Pick the sports you want SportLink to prioritize first.",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
        )

        Sport.entries.forEach { sport ->
            val label = sport.displayName.toNameCase()
            Row(modifier = Modifier.fillMaxWidth()) {
                Checkbox(
                    checked = vm.favouriteSports.contains(sport.displayName),
                    onCheckedChange = { vm.toggleFavouriteSport(sport.displayName) },
                )
                Text(
                    text = label,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        }

        RoundedFilledButton(
            isTappable = vm.favouriteSports.isNotEmpty(),
            action = onContinue,
        ) {
            Text("Continue")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SportsSelectionViewPreview() {
    SportLinkTheme(darkTheme = false, dynamicColor = false) {
        SportsSelectionView(
            vm = SignupVM(),
            showsBackButton = false,
            onContinue = {},
        )
    }
}
