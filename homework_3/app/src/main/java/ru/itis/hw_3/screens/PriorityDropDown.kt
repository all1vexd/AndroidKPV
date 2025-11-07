package ru.itis.hw_3.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.hw_3.domain.model.NotificationPriority

@Composable
fun PriorityDropDown(
    selectedPriority: NotificationPriority,
    onPrioritySelected: (NotificationPriority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val priorities = listOf(
        NotificationPriority.Max to "Максимальный",
        NotificationPriority.High to "Высокий",
        NotificationPriority.Medium to "Средний",
        NotificationPriority.Low to "Низкий"
    )

    val currentLabel = priorities.find {
        it.first == selectedPriority
    }?.second ?: ""

    Box (
      modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = currentLabel,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                )
                .padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            priorities.forEach { (priority, label) ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        onPrioritySelected(priority)
                        expanded = false
                    }
                )
            }
        }
    }
}