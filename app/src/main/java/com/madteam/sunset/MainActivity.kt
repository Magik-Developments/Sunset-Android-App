package com.madteam.sunset

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.madteam.sunset.navigation.SunsetNavigation
import com.madteam.sunset.ui.screens.discover.DiscoverViewModel
import com.madteam.sunset.ui.theme.SunsetTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                discoverViewModel.getDeviceLocation(fusedLocationProviderClient)
            }
        }

    private fun askPermissions() = when (PackageManager.PERMISSION_GRANTED) {
      ContextCompat.checkSelfPermission(
          this,
          ACCESS_FINE_LOCATION
      ) -> {
          discoverViewModel.getDeviceLocation(fusedLocationProviderClient)
      }
      else -> {
          requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
      }
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val discoverViewModel: DiscoverViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SunsetTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    SunsetNavigation()
                }
            }
        }
    }
}