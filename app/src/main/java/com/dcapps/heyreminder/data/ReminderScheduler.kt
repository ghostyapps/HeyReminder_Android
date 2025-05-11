package com.dcapps.heyreminder.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar
import com.dcapps.heyreminder.data.Reminder
import com.dcapps.heyreminder.data.ReminderReceiver
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

object ReminderScheduler {
    fun schedule(context: Context, reminder: Reminder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "reminder_channel"
            val channelName = "Reminder Channel"
            val channelDesc = "Hatırlatma bildirimleri"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDesc
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val dayMapping = mapOf(
            1 to Calendar.MONDAY,
            2 to Calendar.TUESDAY,
            3 to Calendar.WEDNESDAY,
            4 to Calendar.THURSDAY,
            5 to Calendar.FRIDAY,
            6 to Calendar.SATURDAY,
            7 to Calendar.SUNDAY
        )
        reminder.days.forEach { selectedDay ->
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, reminder.hour)
                set(Calendar.MINUTE, reminder.minute)
                set(Calendar.SECOND, 0)
                set(Calendar.DAY_OF_WEEK, dayMapping[selectedDay] ?: Calendar.MONDAY)
                if (before(Calendar.getInstance())) add(Calendar.WEEK_OF_YEAR, 1)
            }
            val intent = Intent(context, ReminderReceiver::class.java).apply {
                putExtra("reminderText", reminder.text)
            }
            val requestCode = (reminder.id * 10 + selectedDay).toInt()
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmMgr.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancel(context: Context, reminder: Reminder) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val dayMapping = mapOf(
            1 to Calendar.MONDAY,
            2 to Calendar.TUESDAY,
            3 to Calendar.WEDNESDAY,
            4 to Calendar.THURSDAY,
            5 to Calendar.FRIDAY,
            6 to Calendar.SATURDAY,
            7 to Calendar.SUNDAY
        )
        reminder.days.forEach { selectedDay ->
            val requestCode = (reminder.id * 10 + selectedDay).toInt()
            val intent = Intent(context, ReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmMgr.cancel(pendingIntent)
        }
    }
}