package com.madteam.sunset.ui.screens.home.ui

import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.madteam.sunset.R

@Composable
fun FeedAdItem(
) {
    val context = LocalContext.current
    val nativeAdFlow = remember { mutableStateOf<NativeAd?>(null) }

    val adLoader = remember {
        AdLoader.Builder(
            context,
            "ca-app-pub-3940256099942544/2247696110"
        ) //TODO: Change UnitID when release to production
            .forNativeAd { nativeAd ->
                nativeAdFlow.value = nativeAd
            }
            .withAdListener(object : AdListener() {

            })
            .build()
    }

    LaunchedEffect(key1 = Unit, block = {
        adLoader.loadAd(AdRequest.Builder().build())
    })

    AndroidView(
        factory = { context ->
            LayoutInflater.from(context)
                .inflate(R.layout.native_ad_feed_item, null) as NativeAdView
        },
        modifier = Modifier.size(370.dp),
        update = { adView ->
            // Find the view IDs defined in the XML layout file
            val adIconView = adView.findViewById<ImageView>(R.id.icon)
            val adHeadlineView = adView.findViewById<TextView>(R.id.primary)
            val adBodyView = adView.findViewById<TextView>(R.id.secondary)
            val adCallToActionView = adView.findViewById<Button>(R.id.cta)
            val adAdvertiserView = adView.findViewById<TextView>(R.id.ad_flag)
            val adMediaView = adView.findViewById<MediaView>(R.id.media_view)

            // Register the view IDs with the native ad view.
            adView.headlineView = adHeadlineView
            adView.bodyView = adBodyView
            adView.callToActionView = adCallToActionView
            adView.iconView = adIconView
            adView.advertiserView = adAdvertiserView
            adView.mediaView = adMediaView

            // Populate the native ad view.
            val nativeAd = nativeAdFlow.value
            nativeAd?.let {
                adIconView.setImageDrawable(nativeAd.icon?.drawable)
                adHeadlineView.text = nativeAd.headline
                adBodyView.text = nativeAd.body
                adCallToActionView.text = nativeAd.callToAction
                adAdvertiserView.text = nativeAd.advertiser

                adMediaView.mediaContent = nativeAd.mediaContent
                adMediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)

                // Register the native ad view with the native ad object.
                adView.setNativeAd(nativeAd)
            }
        }
    )
}