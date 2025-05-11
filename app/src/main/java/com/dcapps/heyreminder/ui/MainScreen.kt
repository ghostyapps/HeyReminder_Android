package com.dcapps.heyreminder.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.dcapps.heyreminder.R
import com.dcapps.heyreminder.data.ReminderRepository
import com.dcapps.heyreminder.data.Reminder
import com.dcapps.heyreminder.data.ReminderScheduler

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    var showCreate by remember { mutableStateOf(false) }
    var selectedReminder by remember { mutableStateOf<Reminder?>(null) }
    var showOptions by remember { mutableStateOf(false) }
    val reminders by rememberUpdatedState(newValue = ReminderRepository.getAll())

    // Renkler
    val backgroundColor = colorResource(R.color.white_background)
    val accentGreen     = colorResource(R.color.accent_color)
    val textColor       = colorResource(R.color.text_color)

    if (showCreate) {
        CreateReminderScreen(
            onBack = { showCreate = false; selectedReminder = null },
            existing = selectedReminder
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            // HEADER (1/4 ekran)
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(accentGreen),
                contentAlignment = Alignment.BottomStart
            ) {
                // Adjust header font size for small screens
                val headerFontSize = if (maxWidth < 420.dp) 28.sp else 42.sp
                val count = reminders.size
                Text(
                    text = "Hey 👋\nYou have $count reminders.",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = headerFontSize,
                    color = textColor,
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                )
            }

            // LİSTE (3/4 ekranın ortası)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(reminders) { reminder ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = { /* boş bırak */ },
                                onLongClick = {
                                    selectedReminder = reminder
                                    showOptions = true
                                }
                            )
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = reminder.text,
                            color = textColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        // Gün ve saat metni
                        val daysLabels = listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")
                        val dayText = if (reminder.days.size == 7) {
                            "Everyday"
                        } else {
                            reminder.days
                                .sorted()
                                .joinToString(", ") { daysLabels[it - 1] }
                        }
                        Text(
                            text = "%02d:%02d - $dayText".format(reminder.hour, reminder.minute),
                            color = textColor,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // ALT BUTON (1/4 ekranın alt kısmı)
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                // Adjust button text size for small screens
                val buttonFontSize = if (maxWidth < 360.dp) 16.sp else 24.sp
                Button(
                    onClick = { showCreate = true },
                    colors = ButtonDefaults.buttonColors(containerColor = accentGreen),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(56.dp)
                ) {
                    Text(
                        text = "New Reminder",
                        color = textColor,
                        fontSize = buttonFontSize,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (showOptions && selectedReminder != null) {
            AlertDialog(
                onDismissRequest = { showOptions = false },
                title = { Text("Reminder Options") },
                text = { Text("What do you want to do?") },
                confirmButton = {
                    Button(onClick = {
                        showCreate = true
                        showOptions = false
                    }) {
                        Text("Edit")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        ReminderScheduler.cancel(context, selectedReminder!!)
                        ReminderRepository.deleteReminder(selectedReminder!!.id)
                        showOptions = false
                    }) {
                        Text("Delete")
                    }
                }
            )
        }
    }
}