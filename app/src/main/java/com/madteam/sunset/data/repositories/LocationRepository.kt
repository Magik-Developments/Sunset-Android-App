package com.madteam.sunset.data.repositories

import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
                emit(address.countryName ?: "")
            }
        }
    }.catch {
        emit("")
        Log.e("LocationRepository::obtainCountryFromLatLng", "Error: ${it.message}")
    }

    override fun obtainLocalityFromLatLng(latLng: LatLng): Flow<String> = flow {
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                var locality = ""
                val address: Address = addresses[0]
                if (address.locality != null) {
                    locality = address.locality
                } else if (address.subLocality != null) {
                    locality = address.subLocality
                }
                emit(locality)
            }
        }
    }.catch {
        emit("")
        Log.e("LocationRepository::obtainLocalityFromLatLng", "Error: ${it.message}")
    }
}

interface LocationContract {
    fun obtainCountryFromLatLng(latLng: LatLng): Flow<String>
    fun obtainLocalityFromLatLng(latLng: LatLng): Flow<String>
}