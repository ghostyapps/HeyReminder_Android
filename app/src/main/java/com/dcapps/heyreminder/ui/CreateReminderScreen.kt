package com.dcapps.heyreminder.ui

import com.dcapps.heyreminder.R

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcapps.heyreminder.data.Reminder
import com.dcapps.heyreminder.data.ReminderRepository
import com.dcapps.heyreminder.data.ReminderScheduler
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.border
import androidx.activity.compose.BackHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReminderScreen(
    onBack: () -> Unit,
    existing: Reminder? = null
) {
    BackHandler {
        onBack()
    }
    val context = LocalContext.current
    val calendarNow = Calendar.getInstance()

    var text by remember { mutableStateOf(existing?.text ?: "") }
    var hour by remember { mutableStateOf(existing?.hour ?: calendarNow.get(Calendar.HOUR_OF_DAY)) }
    var minute by remember { mutableStateOf(existing?.minute ?: calendarNow.get(Calendar.MINUTE)) }

    // Gün seçimi için state
    val daysOfWeek = listOf(1, 2, 3, 4, 5, 6, 7) // Pazartesi-Pazar
    val dayLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val selectedDays = remember {
        mutableStateListOf<Int>().apply {
            existing?.days?.forEach { add(it) }
        }
    }

    // Renkleri resources.xml’den alıyoruz
    val accentGreen = colorResource(R.color.accent_color)
    val bgColor = colorResource(R.color.white_background)
    val textColor = colorResource(R.color.text_color)

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
    ) {
        // Hatırlatma metni girişi
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Reminder text", color = textColor) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = bgColor,
                focusedIndicatorColor = accentGreen,
                cursorColor = accentGreen
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Haftanın günlerini seçme bölümü
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            dayLabels.forEachIndexed { index, label ->
                val dayValue = daysOfWeek[index]
                val isSelected = selectedDays.contains(dayValue)

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .border(
                            width = 2.dp,
                            color = if (isSelected) accentGreen else Color.Gray,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .background(
                            color = if (isSelected) accentGreen.copy(alpha = 0.2f) else Color.Transparent,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable {
                            if (isSelected) selectedDays.remove(dayValue)
                            else selectedDays.add(dayValue)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(label, color = textColor)
                        if (isSelected) {
                            Text("✓", color = textColor)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Saat seçme butonu (analog picker)
        Button(
            onClick = {
                TimePickerDialog(
                    context,
                    { _, h, m ->
                        hour = h
                        minute = m
                    },
                    hour,
                    minute,
                    true
                ).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = accentGreen),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Pick Time: %02d:%02d".format(hour, minute),
                color = textColor,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Kaydet/Güncelle ve İptal butonları
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        val reminder = Reminder(
                            id = existing?.id ?: 0L,
                            text = text,
                            hour = hour,
                            minute = minute,
                            days = selectedDays
                        )
                        val saved = if (existing != null)
                            ReminderRepository.updateReminder(reminder)
                        else
                            ReminderRepository.addReminder(reminder)

                        ReminderScheduler.schedule(context, saved)
                        onBack()
                    }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = accentGreen)
            ) {
                Text(
                    if (existing != null) "Update" else "Save",
                    color = textColor
                )
            }
            Button(
                onClick = { onBack() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = accentGreen)
            ) {
                Text("Cancel", color = textColor)
            }
        }
    }
}