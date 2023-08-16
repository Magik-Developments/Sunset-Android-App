package com.madteam.sunset.utils.googlemaps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.madteam.sunset.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun List<LatLng>.getCenterOfPolygon(): LatLngBounds {
  val centerBuilder: LatLngBounds.Builder = LatLngBounds.builder()
  forEach { centerBuilder.include(LatLng(it.latitude, it.longitude)) }
  return centerBuilder.build()
}

private data class CameraViewCoord(
  val yMax: Double,
  val yMin: Double,
  val xMax: Double,
  val xMin: Double
)

fun List<LatLng>.calculateCameraViewPoints(pctView: Double = .25): List<LatLng> {
  val coordMax = findMaxMins()
  val dy = coordMax.yMax - coordMax.yMin
  val dx = coordMax.xMax - coordMax.xMin
  val yT = (dy * pctView) + coordMax.yMax
  val yB = coordMax.yMin - (dy * pctView)
  val xR = (dx * pctView) + coordMax.xMax
  val xL = coordMax.xMin - (dx * pctView)
  return listOf(
    LatLng(coordMax.xMax, yT),
    LatLng(coordMax.xMin, yB),
    LatLng(xR, coordMax.yMax),
    LatLng(xL, coordMax.yMin)
  )
}

private fun List<LatLng>.findMaxMins(): CameraViewCoord {
  check(isNotEmpty()) { "Cannot calculate the view coordinates of nothing." }
  var viewCoord: CameraViewCoord? = null
  for(point in this) {
    viewCoord = CameraViewCoord(
      yMax = viewCoord?.yMax?.let { yMax ->
        if (point.longitude > yMax) {
          point.longitude
        } else {
          yMax
        }
      } ?: point.longitude,
      yMin = viewCoord?.yMin?.let { yMin->
        if (point.longitude < yMin) {
          point.longitude
        } else {
          yMin
        }
      } ?: point.longitude,
      xMax = viewCoord?.xMax?.let { xMax->
        if (point.latitude > xMax) {
          point.latitude
        } else {
          xMax
        }
      } ?: point.latitude,
      xMin = viewCoord?.xMin?.let { xMin->
        if (point.latitude < xMin) {
          point.latitude
        } else {
          xMin
        }
      } ?: point.latitude,
    )
  }
  return viewCoord ?: throw IllegalStateException("viewCoord cannot be null.")
}

@Composable
fun setMapProperties(mapState: MapState): MapProperties {
  val context = LocalContext.current

  val styleJson = remember {
    context.resources.openRawResource(R.raw.map_style).run {
      bufferedReader().use { it.readText() }
    }
  }

  return MapProperties(
    isMyLocationEnabled = mapState.lastKnownLocation != null,
    mapStyleOptions = MapStyleOptions(styleJson)
  )
}

fun GoogleMap.updateCameraLocation(
  scope: CoroutineScope,
  cameraPositionState: CameraPositionState,
  newLocation: LatLng
) {
  setOnMapLoadedCallback {
    scope.launch {
      cameraPositionState.animate(
        update = CameraUpdateFactory.newLatLngZoom(
          newLocation,
          18f
        )
      )
    }
  }
}