package com.madteam.sunset.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

    @SerializedName("location") var location: WeatherLocation? = WeatherLocation(),
    @SerializedName("current") var current: CurrentWeather? = CurrentWeather(),
    @SerializedName("forecast") var forecast: WeatherForecast? = WeatherForecast()

)

data class WeatherLocation(

    @SerializedName("name") var name: String? = null,
    @SerializedName("region") var region: String? = null,
    @SerializedName("country") var country: String? = null,
    @SerializedName("lat") var lat: Double? = null,
    @SerializedName("lon") var lon: Double? = null,
    @SerializedName("tz_id") var tzId: String? = null,
    @SerializedName("localtime_epoch") var localtimeEpoch: Double? = null,
    @SerializedName("localtime") var localtime: String? = null

)

data class CurrentWeather(

    @SerializedName("last_updated_epoch") var lastUpdatedEpoch: Double? = null,
    @SerializedName("last_updated") var lastUpdated: String? = null,
    @SerializedName("temp_c") var tempC: Double? = null,
    @SerializedName("temp_f") var tempF: Double? = null,
    @SerializedName("is_day") var isDay: Double? = null,
    @SerializedName("condition") var condition: WeatherCondition? = WeatherCondition(),
    @SerializedName("wind_mph") var windMph: Double? = null,
    @SerializedName("wind_kph") var windKph: Double? = null,
    @SerializedName("wind_degree") var windDegree: Double? = null,
    @SerializedName("wind_dir") var windDir: String? = null,
    @SerializedName("pressure_mb") var pressureMb: Double? = null,
    @SerializedName("pressure_in") var pressureIn: Double? = null,
    @SerializedName("precip_mm") var precipMm: Double? = null,
    @SerializedName("precip_in") var precipIn: Double? = null,
    @SerializedName("humidity") var humidity: Double? = null,
    @SerializedName("cloud") var cloud: Double? = null,
    @SerializedName("feelslike_c") var feelsLikeC: Double? = null,
    @SerializedName("feelslike_f") var feelsLikeF: Double? = null,
    @SerializedName("vis_km") var visKm: Double? = null,
    @SerializedName("vis_miles") var visMiles: Double? = null,
    @SerializedName("uv") var uv: Double? = null,
    @SerializedName("gust_mph") var gustMph: Double? = null,
    @SerializedName("gust_kph") var gustKph: Double? = null

)

data class WeatherCondition(

    @SerializedName("text") var text: String? = null,
    @SerializedName("icon") var icon: String? = null,
    @SerializedName("code") var code: Int? = null

)

data class WeatherDay(

    @SerializedName("maxtemp_c") var maxtempC: Double? = null,
    @SerializedName("maxtemp_f") var maxtempF: Double? = null,
    @SerializedName("mintemp_c") var mintempC: Double? = null,
    @SerializedName("mintemp_f") var mintempF: Double? = null,
    @SerializedName("avgtemp_c") var avgtempC: Double? = null,
    @SerializedName("avgtemp_f") var avgtempF: Double? = null,
    @SerializedName("maxwind_mph") var maxwindMph: Double? = null,
    @SerializedName("maxwind_kph") var maxwindKph: Double? = null,
    @SerializedName("totalprecip_mm") var totalprecipMm: Double? = null,
    @SerializedName("totalprecip_in") var totalprecipIn: Double? = null,
    @SerializedName("totalsnow_cm") var totalsnowCm: Double? = null,
    @SerializedName("avgvis_km") var avgvisKm: Double? = null,
    @SerializedName("avgvis_miles") var avgvisMiles: Double? = null,
    @SerializedName("avghumidity") var avghumidity: Double? = null,
    @SerializedName("daily_will_it_rain") var dailyWillItRain: Double? = null,
    @SerializedName("daily_chance_of_rain") var dailyChanceOfRain: Double? = null,
    @SerializedName("daily_will_it_snow") var dailyWillItSnow: Double? = null,
    @SerializedName("daily_chance_of_snow") var dailyChanceOfSnow: Double? = null,
    @SerializedName("condition") var condition: WeatherCondition? = WeatherCondition(),
    @SerializedName("uv") var uv: Double? = null

)

data class WeatherForecast(

    @SerializedName("forecastday") var forecastDay: ArrayList<ForecastDay> = arrayListOf()

)

data class ForecastDay(

    @SerializedName("date") var date: String? = null,
    @SerializedName("date_epoch") var dateEpoch: Double? = null,
    @SerializedName("day") var day: WeatherDay? = WeatherDay(),
    @SerializedName("astro") var astro: WeatherAstro? = WeatherAstro(),
    @SerializedName("hour") var hour: ArrayList<WeatherHour> = arrayListOf()

)

data class WeatherAstro(

    @SerializedName("sunrise") var sunrise: String? = null,
    @SerializedName("sunset") var sunset: String? = null,
    @SerializedName("moonrise") var moonrise: String? = null,
    @SerializedName("moonset") var moonset: String? = null,
    @SerializedName("moon_phase") var moonPhase: String? = null,
    @SerializedName("moon_illumination") var moonIllumination: String? = null,
    @SerializedName("is_moon_up") var isMoonUp: Double? = null,
    @SerializedName("is_sun_up") var isSunUp: Double? = null

)

data class WeatherHour(

    @SerializedName("time_epoch") var timeEpoch: Double? = null,
    @SerializedName("time") var time: String? = null,
    @SerializedName("temp_c") var tempC: Double? = null,
    @SerializedName("temp_f") var tempF: Double? = null,
    @SerializedName("is_day") var isDay: Double? = null,
    @SerializedName("condition") var condition: WeatherCondition? = WeatherCondition(),
    @SerializedName("wind_mph") var windMph: Double? = null,
    @SerializedName("wind_kph") var windKph: Double? = null,
    @SerializedName("wind_degree") var windDegree: Double? = null,
    @SerializedName("wind_dir") var windDir: String? = null,
    @SerializedName("pressure_mb") var pressureMb: Double? = null,
    @SerializedName("pressure_in") var pressureIn: Double? = null,
    @SerializedName("precip_mm") var precipMm: Double? = null,
    @SerializedName("precip_in") var precipIn: Double? = null,
    @SerializedName("humidity") var humidity: Double? = null,
    @SerializedName("cloud") var cloud: Double? = null,
    @SerializedName("feelslike_c") var feelslikeC: Double? = null,
    @SerializedName("feelslike_f") var feelslikeF: Double? = null,
    @SerializedName("windchill_c") var windchillC: Double? = null,
    @SerializedName("windchill_f") var windchillF: Double? = null,
    @SerializedName("heatindex_c") var heatindexC: Double? = null,
    @SerializedName("heatindex_f") var heatindexF: Double? = null,
    @SerializedName("dewpoint_c") var dewpointC: Double? = null,
    @SerializedName("dewpoint_f") var dewpointF: Double? = null,
    @SerializedName("will_it_rain") var willItRain: Double? = null,
    @SerializedName("chance_of_rain") var chanceOfRain: Double? = null,
    @SerializedName("will_it_snow") var willItSnow: Double? = null,
    @SerializedName("chance_of_snow") var chanceOfSnow: Double? = null,
    @SerializedName("vis_km") var visKm: Double? = null,
    @SerializedName("vis_miles") var visMiles: Double? = null,
    @SerializedName("gust_mph") var gustMph: Double? = null,
    @SerializedName("gust_kph") var gustKph: Double? = null,
    @SerializedName("uv") var uv: Double? = null

)
