package com.madteam.sunset.model

data class SunsetTimeResponse(
    val results: SunsetTimeResultsResponse
) {
    constructor() : this(
        SunsetTimeResultsResponse(
            "",
            "",
            ""
        )
    )
}

data class SunsetTimeResultsResponse(
    val sunset: String,
    val golden_hour: String,
    val dusk: String
) {
    constructor() : this(
        sunset = "",
        golden_hour = "",
        dusk = ""
    )
}
