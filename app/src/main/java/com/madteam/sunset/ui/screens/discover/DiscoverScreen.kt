package com.madteam.sunset.ui.screens.discover

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import com.madteam.sunset.ui.common.RoundedCloseIconButton
import com.madteam.sunset.ui.common.SunsetBottomNavigation
import com.madteam.sunset.ui.theme.primaryBoldHeadlineS
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.utils.googlemaps.MapState
import com.madteam.sunset.utils.googlemaps.clusters.CustomClusterRenderer
import com.madteam.sunset.utils.googlemaps.clusters.SpotClusterItem
import com.madteam.sunset.utils.googlemaps.clusters.ZoneClusterManager
import kotlinx.coroutines.launch

const val MAP_PADDING = 0

@Composable
fun DiscoverScreen(
  navController: NavController,
  viewModel: DiscoverViewModel = hiltViewModel()
) {

  val mapState by viewModel.mapState.collectAsStateWithLifecycle()
  val selectedCluster by viewModel.selectedCluster.collectAsStateWithLifecycle()

  Scaffold(
    bottomBar = { SunsetBottomNavigation(navController) },
    content = { paddingValues ->
      Box(
        modifier = Modifier.padding(paddingValues),
        contentAlignment = Alignment.Center
      ) {
        DiscoverContent(
          mapState = mapState,
          selectedCluster = { clusterItem ->
            viewModel.selectedCluster.value = clusterItem
          },
          calculateZoneViewCenter = viewModel::calculateZoneLatLngBounds,
          onClusterClicked = { clusterItem ->
            viewModel.selectedCluster.value = clusterItem
          }
        )

        if (selectedCluster != null) {
          Box(
            modifier = Modifier
              .align(Alignment.BottomCenter)
              .padding(24.dp)
          ) {
            TestSelectedComposable(selectedCluster!!) { viewModel.selectedCluster.value = null }
          }
        }

      }
    }
  )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable fun TestSelectedComposable(selectedCluster: SpotClusterItem, onClose: () -> Unit) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .height(140.dp),
    backgroundColor = Color.White,
    shape = RoundedCornerShape(20.dp),
    elevation = 2.dp,
  ) {
    Row(modifier = Modifier.fillMaxWidth()) {
      Box() {
        GlideImage(
          modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.4f),
          model = "https://t4.ftcdn.net/jpg/01/04/78/75/360_F_104787586_63vz1PkylLEfSfZ08dqTnqJqlqdq0eXx.jpg",
          contentDescription = "",
          contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.padding(8.dp)) {
          RoundedCloseIconButton { onClose() }
        }
      }
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(8.dp)
      ) {
        Text(text = selectedCluster.title, style = primaryBoldHeadlineS)
        Text(text = "Lorem ipsum", style = secondaryRegularBodyL)
      }
    }
  }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun DiscoverContent(
  mapState: MapState,
  selectedCluster: (SpotClusterItem) -> Unit,
  calculateZoneViewCenter: () -> LatLngBounds,
  onClusterClicked: (SpotClusterItem) -> Unit
) {

  val styleJson = """
    [
        {
            "featureType": "poi",
            "elementType": "labels",
            "stylers": [
                {
                    "visibility": "off"
                }
            ]
        },
        {
            "featureType": "road",
            "elementType": "labels",
            "stylers": [
                {
                    "visibility": "off"
                }
            ]
        },
        {
            "featureType": "water",
            "elementType": "geometry",
            "stylers": [
                {
                    "color": "#CDE2F0"
                }
            ]
        },
        {
            "featureType": "poi",
            "elementType": "labels.icon",
            "stylers": [
                {
                    "visibility": "off"
                }
            ]
        }
    ]
""".trimIndent()

  val context = LocalContext.current
  val mapProperties = MapProperties(
    isMyLocationEnabled = mapState.lastKnownLocation != null,
    mapStyleOptions = MapStyleOptions(styleJson)
  )
  val cameraPositionState = rememberCameraPositionState()

  GoogleMap(
    modifier = Modifier.fillMaxSize(),
    properties = mapProperties,
    cameraPositionState = cameraPositionState,
    uiSettings = MapUiSettings()
  ) {

    val scope = rememberCoroutineScope()

    MapEffect(mapState.clusterItems) { map ->
      if (mapState.clusterItems.isNotEmpty()) {
        val clusterManager = ZoneClusterManager(context, map)
        val clusterRenderer = CustomClusterRenderer(context, map, clusterManager)
        clusterManager.addItems(mapState.clusterItems)
        clusterManager.setOnClusterClickedListener { clusterItem ->
          selectedCluster(clusterItem)
        }
        clusterManager.renderer = clusterRenderer
        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)
        clusterManager.setOnClusterClickListener { cluster ->
          val clusterItem = cluster.items.firstOrNull()
          clusterItem?.let { onClusterClicked(it) }
          true
        }
        map.setOnMapLoadedCallback {
          if (mapState.clusterItems.isNotEmpty()) {
            scope.launch {
              cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngBounds(
                  calculateZoneViewCenter(),
                  MAP_PADDING
                )
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun getClusterManager(context: Context, map: GoogleMap): ZoneClusterManager =
  ZoneClusterManager(context, map)
