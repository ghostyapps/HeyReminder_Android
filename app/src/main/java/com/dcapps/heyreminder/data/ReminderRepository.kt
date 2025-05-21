package com.dcapps.heyreminder.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow

object ReminderRepository {
    private lateinit var prefs: SharedPreferences
    private val gson = Gson()
    private val type = object : TypeToken<List<Reminder>>() {}.type
    private val _remindersFlow = MutableStateFlow<List<Reminder>>(emptyList())
    val remindersFlow: StateFlow<List<Reminder>> get() = _remindersFlow

    fun init(context: Context) {
        prefs = context.getSharedPreferences("rem_prefs", Context.MODE_PRIVATE)
        val list = loadList()
        _remindersFlow.value = list
    }

    fun loadList(): MutableList<Reminder> {
        val json = prefs.getString("reminders", null)
        return if (json.isNullOrEmpty()) mutableListOf() else gson.fromJson(json, type)
    }

    private fun saveList(list: List<Reminder>) {
        prefs.edit().putString("reminders", gson.toJson(list)).apply()
    }

    private fun generateId(list: List<Reminder>): Long =
        (list.maxOfOrNull { it.id } ?: 0L) + 1L

    fun getAll(): Flow<List<Reminder>> = remindersFlow

    suspend fun addReminder(reminder: Reminder): Reminder {
        val list = loadList()
        val toAdd = if (reminder.id == 0L) reminder.copy(id = generateId(list)) else reminder
        list.add(toAdd)
        saveList(list)
        _remindersFlow.value = list
        return toAdd
    }

    suspend fun deleteReminder(reminder: Reminder) {
        val list = loadList()
        list.removeAll { it.id == reminder.id }
        saveList(list)
        _remindersFlow.value = list
    }

    suspend fun updateReminder(reminder: Reminder): Reminder {
        val list = loadList()
        val index = list.indexOfFirst { it.id == reminder.id }
        if (index != -1) list[index] = reminder else list.add(reminder)
        saveList(list)
        _remindersFlow.value = list
        return reminder
    }

    fun getAllSync(): List<Reminder> = loadList()
}