package com.madteam.sunset.utils

import com.madteam.sunset.data.model.WeatherResponse
import kotlin.math.abs

val skyConditionsValues = mapOf(
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

fun calculateNumericScore(
    value: Double,
    desiredValue: Double,
    maxValue: Double
): Double {
    return 100 - abs(value - desiredValue) / maxValue * 100
}

fun calculateCategoricalScore(
    value: Int,
    valuesMap: Map<Int, Int>
): Int {
    return valuesMap.getOrDefault(value, 0)
}

fun calculateFinalScore(
    scores: List<Double>,
    weights: List<Double>
): Int {
    return (scores.zip(weights) { score, weight -> score * weight }.sum()).toInt().coerceIn(0, 100)
}

fun calculateSunsetScore(
    weatherInfo: WeatherResponse,
    targetDay: String
): Int {
    val temperatureWeight = 0.2
    val conditionWeight = 0.3
    val visibilityWeight = 0.2
    val humidityWeight = 0.2

    val desiredTemperature = 21.0

    var sunsetTemperature: Double = Double.NaN
    var weatherCondition: Int = -1
    var visibility: Double = Double.NaN
    var humidityValue: Double = Double.NaN

    val matchingForecast = weatherInfo.forecast?.forecastDay?.find { it.date == targetDay }

    if (matchingForecast != null && matchingForecast.hour.isNotEmpty()) {
        val selectedHour = matchingForecast.hour.first()

        with(selectedHour) {
            sunsetTemperature = tempC ?: Double.NaN
            weatherCondition = condition?.code ?: -1
            visibility = visKm ?: Double.NaN
            humidityValue = humidity ?: Double.NaN
        }
    }

    val temperatureScore =
        calculateNumericScore(sunsetTemperature, desiredTemperature, maxValue = 50.0)

    val conditionScore = calculateCategoricalScore(weatherCondition, skyConditionsValues)

    val visibilityScore = calculateNumericScore(visibility, desiredValue = 10.0, maxValue = 10.0)

    val humidityScore = calculateNumericScore(humidityValue, desiredValue = 60.0, maxValue = 100.0)

    return calculateFinalScore(
        scores = listOf(
            temperatureScore,
            conditionScore.toDouble(),
            visibilityScore,
            humidityScore
        ),
        weights = listOf(temperatureWeight, conditionWeight, visibilityWeight, humidityWeight)
    )
}