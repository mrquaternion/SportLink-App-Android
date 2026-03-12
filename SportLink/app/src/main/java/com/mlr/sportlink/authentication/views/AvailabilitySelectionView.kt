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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mlr.sportlink.authentication.viewmodels.AvailabilityWindow
import com.mlr.sportlink.authentication.viewmodels.SignupVM
import com.mlr.sportlink.authentication.views.components.RoundedFilledButton
import com.mlr.sportlink.authentication.views.components.TimeSlotEditor
import com.mlr.sportlink.ui.theme.SportLinkTheme

enum class DayOfWeek(
    val storageKey: String,
    val label: String,
    val short: String,
) {
    MONDAY("monday", "Monday", "Mon"),
    TUESDAY("tuesday", "Tuesday", "Tue"),
    WEDNESDAY("wednesday", "Wednesday", "Wed"),
    THURSDAY("thursday", "Thursday", "Thu"),
    FRIDAY("friday", "Friday", "Fri"),
    SATURDAY("saturday", "Saturday", "Sat"),
    SUNDAY("sunday", "Sunday", "Sun"),
}

data class TimeSlot(
    val startMinutes: Int = 18 * 60,
    val endMinutes: Int = 20 * 60,
) {
    fun formatted(): String = "${startMinutes.formatClock()} - ${endMinutes.formatClock()}"
}

@Composable
fun AvailabilitySelectionView(
    vm: SignupVM,
    onBack: () -> Unit = {},
    onContinue: () -> Unit,
) {
    var editingDay by remember { mutableStateOf<DayOfWeek?>(null) }
    var editingSlot by remember { mutableStateOf(TimeSlot()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TextButton(onClick = onBack) {
            Text("Back")
        }

        Text(
            text = "Your weekly availability",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Only the checkbox adds the first row. Use All day or Add slot for more detail.",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
        )

        DayOfWeek.entries.forEach { day ->
            val slots = vm.availabilities[day.storageKey].orEmpty()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Checkbox(
                        checked = slots.isNotEmpty(),
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                vm.setAvailability(
                                    day.storageKey,
                                    listOf(TimeSlot().toAvailabilityWindow()),
                                )
                            } else {
                                vm.setAvailability(day.storageKey, emptyList())
                            }
                        },
                    )
                    Text(
                        text = day.label,
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 12.dp),
                    )
                    TextButton(
                        onClick = {
                            vm.setAvailability(
                                day.storageKey,
                                listOf(TimeSlot(9 * 60, 21 * 60).toAvailabilityWindow()),
                            )
                        },
                    ) {
                        Text("All day")
                    }
                    TextButton(
                        onClick = {
                            editingDay = day
                            editingSlot = TimeSlot()
                        },
                    ) {
                        Text("Add slot")
                    }
                }

                if (slots.isEmpty()) {
                    Text(
                        text = "No slots selected",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    )
                } else {
                    slots.forEachIndexed { index, slot ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "${slot.startLabel} - ${slot.endLabel}",
                                modifier = Modifier.weight(1f),
                            )
                            TextButton(
                                onClick = { vm.removeAvailabilityWindow(day.storageKey, index) },
                            ) {
                                Text("Remove")
                            }
                        }
                    }
                }
            }
        }

        RoundedFilledButton(
            action = onContinue,
        ) {
            Text("Continue")
        }
    }

    val activeDay = editingDay
    if (activeDay != null) {
        TimeSlotEditor(
            day = activeDay,
            slot = editingSlot,
            onSlotChange = { editingSlot = it },
            onSave = {
                vm.addAvailabilityWindow(activeDay.storageKey, editingSlot.toAvailabilityWindow())
                editingDay = null
            },
            onDismiss = { editingDay = null },
        )
    }
}

private fun TimeSlot.toAvailabilityWindow(): AvailabilityWindow =
    AvailabilityWindow(
        startLabel = startMinutes.formatClock(),
        endLabel = endMinutes.formatClock(),
    )

private fun Int.formatClock(): String {
    val hour = this / 60
    val minute = this % 60
    return "%02d:%02d".format(hour, minute)
}

@Preview(showBackground = true)
@Composable
private fun AvailabilitySelectionViewPreview() {
    SportLinkTheme(darkTheme = false, dynamicColor = false) {
        AvailabilitySelectionView(
            vm = SignupVM(),
            onContinue = {},
        )
    }
}
