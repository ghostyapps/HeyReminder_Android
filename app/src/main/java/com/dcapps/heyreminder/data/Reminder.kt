package com.dcapps.heyreminder.data

data class Reminder(
    val id: Long = 0,
    val text: String,
    val hour: Int,
    val minute: Int,
    val days: List<Int>
)
