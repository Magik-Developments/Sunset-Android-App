package com.madteam.sunset.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.google.firebase.firestore.GeoPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@DrawableRes
fun getResourceId(icon: String, context: Context): Int {
    return context.resources.getIdentifier(icon, "drawable", context.packageName)
}

fun openDirectionsOnGoogleMaps(context: Context, location: GeoPoint) {
    val uri = Uri.parse("google.navigation:q=${location.latitude},${location.longitude}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")
    context.startActivity(intent)
}

//Shimmer to use in modifiers on any composable
@Composable
fun shimmerBrush(showShimmer: Boolean = true, targetValue: Float = 1000f): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
        )

        val transition = rememberInfiniteTransition()
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(800), repeatMode = RepeatMode.Reverse
            )
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}

fun formatDate(originalDate: String): String {
    val originalFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val date: Date = originalFormat.parse(originalDate) as Date
    return formattedDate.format(date)
}

fun getTimezone(): String {
    val timeZone = TimeZone.getDefault()
    return timeZone.id
}

fun convertHourToMilitaryFormat(originalHour: String): String {
    val originalFormat = SimpleDateFormat("h:mm:ss a", Locale.ENGLISH)
    val desiredFormat = SimpleDateFormat("HH:mm")

    try {
        val date = originalFormat.parse(originalHour)
        return desiredFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ""
}

fun getCurrentTimeFormattedIn24H(): String {
    val currentTime = Date()
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(currentTime)
}

fun calculateTimeDifference(targetTime: String): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val currentTime = getCurrentTimeFormattedIn24H()

    try {
        val currentTimeDate = timeFormat.parse(currentTime)
        val targetTimeDate = timeFormat.parse(targetTime)

        val timeDifferenceMillis = targetTimeDate.time - currentTimeDate.time

        return if (timeDifferenceMillis < 0) {
            val adjustedTimeDifferenceMillis = timeDifferenceMillis + 24 * 60 * 60 * 1000
            val hours = adjustedTimeDifferenceMillis / (60 * 60 * 1000)
            val minutes = (adjustedTimeDifferenceMillis % (60 * 60 * 1000)) / (60 * 1000)
            "$hours h $minutes min"
        } else {
            val hours = timeDifferenceMillis / (60 * 60 * 1000)
            val minutes = (timeDifferenceMillis % (60 * 60 * 1000)) / (60 * 1000)
            "$hours h $minutes min"
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ""
}