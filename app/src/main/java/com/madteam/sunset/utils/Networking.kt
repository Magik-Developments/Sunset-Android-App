package com.madteam.sunset.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getRetrofitInstance(
    baseUrl: String,
    converterFactory: GsonConverterFactory
): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(converterFactory)
        .build()