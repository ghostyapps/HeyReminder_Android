package com.dcapps.heyreminder.data
import com.dcapps.heyreminder.R

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val text = intent.getStringExtra("reminderText") ?: return
        val channelId = "reminder_channel"
        val notif = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_hey_hand)
            .setContentTitle("Hey 👋")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), notif)
    }
}