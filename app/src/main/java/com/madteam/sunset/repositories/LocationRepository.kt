package com.madteam.sunset.repositories

import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val geocoder: Geocoder
) : LocationContract {
    override fun obtainCountryFromLatLng(latLng: LatLng): Flow<String> = flow {
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                emit(address.countryName)
            }
        }
    }

    override fun obtainLocalityFromLatLng(latLng: LatLng): Flow<String> = flow {
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                emit(address.locality)
            }
        }
    }
}

interface LocationContract {
    fun obtainCountryFromLatLng(latLng: LatLng): Flow<String>
    fun obtainLocalityFromLatLng(latLng: LatLng): Flow<String>
}