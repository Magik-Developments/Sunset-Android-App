package com.madteam.sunset.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.google.firebase.firestore.GeoPoint
import com.madteam.sunset.data.model.WeatherResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs

fun openDirectionsOnGoogleMaps(context: Context, location: GeoPoint) {
    val uri = Uri.parse("google.navigation:q=${location.latitude},${location.longitude}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")
    context.startActivity(intent)
}

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

        val hours = timeDifferenceMillis / (60 * 60 * 1000)
        val minutes = (timeDifferenceMillis % (60 * 60 * 1000)) / (60 * 1000)

        return if (hours == 0L) {
            "$minutes min"
        } else if (timeDifferenceMillis < 0) {
            val adjustedTimeDifferenceMillis = timeDifferenceMillis + 24 * 60 * 60 * 1000
            val adjustedHours = adjustedTimeDifferenceMillis / (60 * 60 * 1000)
            val adjustedMinutes = (adjustedTimeDifferenceMillis % (60 * 60 * 1000)) / (60 * 1000)
            "$adjustedHours h $adjustedMinutes min"
        } else {
            "$hours h $minutes min"
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ""
}

fun getShareIntent(
    shareText: String,
    uriToImage: Uri?
): Intent {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
        if (uriToImage != null) {
            putExtra(Intent.EXTRA_STREAM, uriToImage)
            type = "image/jpeg"
        }
    }
    return Intent.createChooser(sendIntent, null)
}

fun generateDeepLink(
    screen: String,
    param: String
): String {
    return when (screen) {
        "spot" -> {
            "https://sunsetapp.es/spotReference=$param"
        }

        "post" -> {
            "https://sunsetapp.es/postReference=$param"
        }

        else -> {
            ""
        }
    }
}

@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }

    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)

        onDispose {
            backCallback.remove()
        }
    }
}

fun calculateSunsetScore(weatherInfo: WeatherResponse, targetDay: String): Int {
    //Ponderations
    val temperatureWeight = 0.3
    val conditionWeight = 0.3
    val visibilityWeight = 0.2
    val humidityWeight = 0.2

    val desiredTemperature = 21
    val conditionsValues = mapOf(
        1000 to 100,   // Sunny
        1003 to 85,    // Partly cloudy
        1006 to 70,    // Cloudy
        1009 to 60,    // Overcast
        1030 to 75,    // Mist
        1063 to 40,    // Patchy rain possible
        1066 to 40,    // Patchy snow possible
        1069 to 40,    // Patchy sleet possible
        1072 to 40,    // Patchy freezing drizzle possible
        1087 to 30,    // Thundery outbreaks possible
        1114 to 20,    // Blowing snow
        1117 to 10,    // Blizzard
        1135 to 50,    // Fog
        1147 to 50,    // Freezing fog
        1150 to 30,    // Patchy light drizzle
        1153 to 30,    // Light drizzle
        1168 to 20,    // Freezing drizzle
        1171 to 10,    // Heavy freezing drizzle
        1180 to 40,    // Patchy light rain
        1183 to 30,    // Light rain
        1186 to 20,    // Moderate rain at times
        1189 to 15,    // Moderate rain
        1192 to 10,    // Heavy rain at times
        1195 to 0,     // Heavy rain
        1198 to 20,    // Light freezing rain
        1201 to 10,    // Moderate or heavy freezing rain
        1204 to 20,    // Light sleet
        1207 to 10,    // Moderate or heavy sleet
        1210 to 30,    // Patchy light snow
        1213 to 20,    // Light snow
        1216 to 15,    // Patchy moderate snow
        1219 to 10,    // Moderate snow
        1222 to 5,     // Patchy heavy snow
        1225 to 0,     // Heavy snow
        1237 to 25,    // Ice pellets
        1240 to 30,    // Light rain shower
        1243 to 20,    // Moderate or heavy rain shower
        1246 to 10,    // Torrential rain shower
        1249 to 20,    // Light sleet showers
        1252 to 10,    // Moderate or heavy sleet showers
        1255 to 30,    // Light snow showers
        1258 to 20,    // Moderate or heavy snow showers
        1261 to 25,    // Light showers of ice pellets
        1264 to 15,    // Moderate or heavy showers of ice pellets
        1273 to 30,    // Patchy light rain with thunder
        1276 to 15,    // Moderate or heavy rain with thunder
        1279 to 20,    // Patchy light snow with thunder
        1282 to 10     // Moderate or heavy snow with thunder
    )

    var sunsetTemperature: Double
    var weatherCondition: Int

    var temperatureScore = 0.0
    var conditionScore = 0
    var visibilityScore = 0.0
    var humidityScore = 0.0

    if (weatherInfo.forecast?.forecastDay != null) {
        val matchingForecast = weatherInfo.forecast!!.forecastDay.find { it.date == targetDay }

        if (matchingForecast != null && matchingForecast.hour.isNotEmpty()) {
            val selectedHour = matchingForecast.hour.first()

            with(selectedHour) {
                sunsetTemperature = tempC!!
                temperatureScore = 100 - abs(sunsetTemperature - desiredTemperature)
                weatherCondition = condition?.code ?: 0
                conditionScore = conditionsValues.getOrDefault(weatherCondition, 0)
                visibilityScore = (visKm!! / 10.0 * 100)
                humidityScore = ((100 - humidity!!) * 0.5)
            }
        }
    }
    val finalScore = (
            temperatureWeight * temperatureScore
                    + conditionWeight * conditionScore
                    + visibilityWeight * visibilityScore
                    + humidityWeight * humidityScore
            ).toInt()

    return finalScore.coerceIn(0, 100)
}

fun calculateSunsetTemperature(weatherInfo: WeatherResponse, targetDay: String): Double {
    val forecastDay = weatherInfo.forecast?.forecastDay

    if (forecastDay != null) {
        val matchingForecast = forecastDay.find { it.date == targetDay }

        if (matchingForecast != null && matchingForecast.hour.isNotEmpty()) {
            val selectedHour = matchingForecast.hour.first()
            return selectedHour.tempC ?: 0.0
        }
    }

    return 0.0
}

fun obtainDateOnFormat(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd")
    val inputDate = inputFormat.parse(dateString)
    val format = SimpleDateFormat("dd MMMM, EEEE", Locale.getDefault())
    return format.format(inputDate ?: "")
}