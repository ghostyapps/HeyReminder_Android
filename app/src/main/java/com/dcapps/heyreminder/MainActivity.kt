package com.dcapps.heyreminder
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



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        // Make system bars match our light-gray background
        window.statusBarColor     = ContextCompat.getColor(this, R.color.accent_color)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.accent_color)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

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

        setContent {
            ReminderAppTheme {
                MainScreen()
            }
        }
    }
}
