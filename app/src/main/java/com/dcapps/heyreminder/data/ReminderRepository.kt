package com.dcapps.heyreminder.data

object ReminderRepository {
    private val reminders = mutableListOf<Reminder>()
    private var idCounter = 1L

    fun addReminder(reminder: Reminder): Reminder {
        val toSave = reminder.copy(id = idCounter++)
        reminders.add(toSave)
        return toSave
    }

    fun deleteReminder(id: Long) {
        reminders.removeAll { it.id == id }
    }


    fun updateReminder(reminder: Reminder): Reminder {
        val index = reminders.indexOfFirst { it.id == reminder.id }
        return if (index != -1) {
            reminders[index] = reminder
            reminder
        } else {
            addReminder(reminder)
        }
    }



    fun getAll(): List<Reminder> = reminders.toList()
}