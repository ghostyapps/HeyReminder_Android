package com.dcapps.heyreminder.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dcapps.heyreminder.R

// Define your custom fonts in res/font (e.g., my_custom_font_regular.ttf, my_custom_font_medium.ttf, my_custom_font_bold.ttf)
private val CustomFontFamily = FontFamily(
    Font(R.font.productsans_medium, FontWeight.Normal),
    Font(R.font.productsans_bold, FontWeight.Medium),
    Font(R.font.productsans_black, FontWeight.Bold)
)

// Define typography using your custom font
private val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp
    ),
    titleLarge = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)

@Composable
fun ReminderAppTheme(content: @Composable () -> Unit) {
    // Load colors from resources
    val primary       = colorResource(R.color.accent_color)
    val onPrimary     = colorResource(R.color.text_color)
    val background    = colorResource(R.color.white_background)
    val onBackground  = colorResource(R.color.text_color)
    val surface       = background
    val onSurface     = onBackground
    val colors = darkColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface
    )
    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}