package com.dcapps.heyreminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dcapps.heyreminder.data.ReminderRepository
import com.dcapps.heyreminder.data.ReminderScheduler

class ReminderBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            ReminderRepository.init(context)
            val reminders = ReminderRepository.getAllSync()
            for (reminder in reminders) {
                ReminderScheduler.schedule(context, reminder)
            }
        }
    }
}