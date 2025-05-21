package com.dcapps.heyreminder
import com.dcapps.heyreminder.data.ReminderScheduler
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.provider.Settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dcapps.heyreminder.ui.MainScreen
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.dcapps.heyreminder.ui.theme.ReminderAppTheme

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat

import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatActivity
import com.dcapps.heyreminder.data.ReminderRepository

import android.content.res.Configuration


import android.graphics.Color


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        // Make system bars match our light-gray background
        window.statusBarColor     = ContextCompat.getColor(this, R.color.accent_color)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.white_background)
        val isDarkTheme = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = !isDarkTheme

        // (1) Exact alarm izni isteme kodu…

        // (2) Bildirim kanalı oluşturma…

        // (3) Android 13+ bildirim izni iste
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }


        // SharedPreferences tabanlı hatırlatıcı deposunu başlat
        ReminderRepository.init(applicationContext)

        // Reschedule all reminders on app start
        val context = applicationContext
        val reminders = ReminderRepository.getAllSync()
        for (reminder in reminders) {
            ReminderScheduler.schedule(context, reminder)
        }

        setContent {
            ReminderAppTheme {
                MainScreen()
            }
        }
    }
}
