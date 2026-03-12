package com.mlr.sportlink.authentication.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.mlr.sportlink.authentication.views.DayOfWeek
import com.mlr.sportlink.authentication.views.TimeSlot

@Composable
fun TimeSlotEditor(
    day: DayOfWeek,
    slot: TimeSlot,
    onSlotChange: (TimeSlot) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
) {
    val presets = listOf(
        "Morning" to TimeSlot(startMinutes = 8 * 60, endMinutes = 12 * 60),
        "Afternoon" to TimeSlot(startMinutes = 13 * 60, endMinutes = 17 * 60),
        "Evening" to TimeSlot(startMinutes = 18 * 60, endMinutes = 22 * 60),
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Set time for ${day.label}")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Quick presets",
                    style = MaterialTheme.typography.titleSmall,
                )
                presets.forEach { (label, preset) ->
                    TextButton(onClick = { onSlotChange(preset) }) {
                        Text("$label: ${preset.formatted()}")
                    }
                }
                Text(
                    text = "Current: ${slot.formatted()}",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}
