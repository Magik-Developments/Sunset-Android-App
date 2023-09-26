package com.madteam.sunset.utils

import android.graphics.BlurMaskFilter
import android.graphics.BlurMaskFilter.Blur.NORMAL
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import com.madteam.sunset.model.Spot
import com.madteam.sunset.model.SpotPost
import com.madteam.sunset.model.UserProfile

fun Modifier.shadow(
    color: Color = Color.Black,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0f.dp,
    modifier: Modifier = Modifier
) = this.then(
    modifier.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spread.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel = (this.size.height + spreadPixel)

            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), NORMAL))
            }

            frameworkPaint.color = color.toArgb()
            it.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint
            )
        }
    })


fun DocumentSnapshot.toSpot(): Spot {
    val id = id
    val creationDate = getString("creation_date")
    val featuredImages = get("featured_images") as List<String>
    val name = getString("name")
    val description = getString("description")
    val score = getDouble("score")
    val visitedTimes = get("visited_times")
    val likes = get("likes")
    val locationInLatLng = getGeoPoint("location_in_latlng")
    val location = getString("location")

    return Spot(
        id = id,
        spottedBy = UserProfile(),
        featuredImages = featuredImages,
        creationDate = creationDate ?: "",
        name = name ?: "",
        description = description ?: "",
        score = score?.toFloat() ?: 0.0f,
        visitedTimes = visitedTimes.toString().toIntOrNull() ?: 0,
        likes = likes.toString().toIntOrNull() ?: 0,
        locationInLatLng = locationInLatLng ?: GeoPoint(0.0, 0.0),
        location = location ?: "",
        attributes = listOf(),
        spotReviews = listOf(),
        spotPosts = listOf(),
        likedBy = listOf()
    )
}


fun DocumentSnapshot.toSpotPost(): SpotPost {
    val images = get("images") as List<String>
    val id = id
    val creationDate = getString("creation_date")
    val description = getString("description")
    val likes = getDouble("likes")
    val spotRef = getDocumentReference("spot")
    return SpotPost(
        id = id,
        description = description ?: "",
        spotRef = spotRef?.path ?: "",
        images = images,
        author = UserProfile(),
        creation_date = creationDate ?: "",
        comments = listOf(),
        likedBy = listOf(),
        likes = likes?.toInt() ?: 0
    )
}