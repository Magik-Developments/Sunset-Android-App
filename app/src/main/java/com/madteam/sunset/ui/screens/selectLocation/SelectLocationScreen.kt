package com.madteam.sunset.ui.screens.selectLocation

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.madteam.sunset.R
import com.madteam.sunset.ui.common.GoForwardTopAppBar
import com.madteam.sunset.utils.googlemaps.MapState
import com.madteam.sunset.utils.googlemaps.setMapProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SelectLocationScreen(
    navController: NavController,
    viewModel: SelectLocationViewModel = hiltViewModel()
) {

    val mapState by viewModel.mapState.collectAsStateWithLifecycle()
    val selectedLocation by viewModel.selectedLocation.collectAsStateWithLifecycle()
    val userLocation by viewModel.userLocation.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            GoForwardTopAppBar(
                title = R.string.add_location,
                onQuitClick = { navController.popBackStack() },
                onContinueClick = { /*TODO*/ },
                canContinue = false
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                SelectLocationContent(
                    mapState = mapState,
                    selectedLocation = selectedLocation,
                    userLocation = userLocation,
                    updateSelectedLocation = viewModel::updateSelectedLocation,
                    updateUserLocation = viewModel::updateUserLocation
                )
            }
        }
    )
}

@Composable
fun SelectLocationContent(
    mapState: MapState,
    selectedLocation: LatLng,
    userLocation: LatLng,
    updateSelectedLocation: (LatLng) -> Unit,
    updateUserLocation: (LatLng) -> Unit
) {

    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    getCurrentLocation(context) { lat, long ->
                        updateUserLocation(LatLng(lat, long))
                    }
                }
            }
        )

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = setMapProperties(mapState = mapState),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false
        )
    ) {
        SetupClusterManagerAndRenderers(
            cameraPositionState = cameraPositionState,
            userLocation = userLocation,
            selectedLocation = selectedLocation,
            updateSelectedLocation = updateSelectedLocation
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End
    ) {
        IconButton(
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFFFFB600)),
            onClick = {
                if (hasLocationPermission(context)) {
                    getCurrentLocation(context) { lat, long ->
                        updateUserLocation(LatLng(lat, long))
                    }
                } else {
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}

private fun GoogleMap.setupUserLocation(
    scope: CoroutineScope,
    cameraPositionState: CameraPositionState,
    latLng: LatLng
) {
    setOnMapLoadedCallback {
        scope.launch {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    latLng,
                    18f
                )
            )
        }
    }
}

@SuppressLint("PotentialBehaviorOverride")
@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun SetupClusterManagerAndRenderers(
    cameraPositionState: CameraPositionState,
    userLocation: LatLng,
    updateSelectedLocation: (LatLng) -> Unit,
    selectedLocation: LatLng
) {

    val scope = rememberCoroutineScope()

    MapEffect(key1 = selectedLocation) { map ->
        map.setOnMapClickListener { clickedLatLng ->
            updateSelectedLocation(clickedLatLng)
        }
        if (selectedLocation.latitude != 0.0 && selectedLocation.longitude != 0.0) {
            map.clear()
            map.addMarker(
                MarkerOptions().position(selectedLocation)
            )
        }
        if (userLocation.longitude != 0.0 && userLocation.latitude != 0.0) {
            map.setupUserLocation(scope, cameraPositionState, userLocation)
        }
    }
}

fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

@SuppressLint("MissingPermission")
fun getCurrentLocation(context: Context, callback: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val long = location.longitude
                callback(lat, long)
            }
        }
        .addOnFailureListener { exception ->
            // Handle location retrieval failure
            exception.printStackTrace()
        }
}
