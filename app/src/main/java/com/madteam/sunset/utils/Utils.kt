package com.madteam.sunset.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import com.google.firebase.firestore.GeoPoint

@DrawableRes
fun getResourceId(icon: String, context: Context): Int {
    return context.resources.getIdentifier(icon, "drawable", context.packageName)
}

fun openDirectionsOnGoogleMaps(context: Context, location: GeoPoint) {
    val uri = Uri.parse("google.navigation:q=${location.latitude},${location.longitude}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")
    context.startActivity(intent)
}