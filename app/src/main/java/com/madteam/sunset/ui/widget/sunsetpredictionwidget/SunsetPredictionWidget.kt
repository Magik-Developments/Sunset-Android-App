package com.madteam.sunset.ui.widget.sunsetpredictionwidget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.madteam.sunset.R

class SunsetPredictionWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Box(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(225.dp)
                    .background(Color(0xFFFFB600))
                    .cornerRadius(20.dp)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = GlanceModifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "86%",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 50.sp,
                                color = ColorProvider(Color.White)
                            )
                        )
                        Text(
                            text = "Sunset quality",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = ColorProvider(Color.White)
                            )
                        )
                    }
                }
                Row(
                    modifier = GlanceModifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = GlanceModifier.defaultWeight()
                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.ic_sunset),
                            contentDescription = "",
                            modifier = GlanceModifier.size(50.dp)
                        )
                        Text(
                            text = "Sunset",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = ColorProvider(Color.White)
                            )
                        )
                        Text(
                            text = "15:34",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = ColorProvider(Color.White)
                            )
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = GlanceModifier.defaultWeight()
                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.ic_golden_hour),
                            contentDescription = "",
                            modifier = GlanceModifier.size(50.dp)
                        )
                        Text(
                            text = "Golden hour",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = ColorProvider(Color.White)
                            )
                        )
                        Text(
                            text = "15:34",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = ColorProvider(Color.White)
                            )
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = GlanceModifier.defaultWeight()
                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.ic_blue_hour),
                            contentDescription = "",
                            modifier = GlanceModifier.size(50.dp)
                        )
                        Text(
                            text = "Blue hour",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = ColorProvider(Color.White)
                            )
                        )
                        Text(
                            text = "15:34",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = ColorProvider(Color.White)
                            )
                        )
                    }
                }
            }
        }
    }
}

class SunsetPredictionWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SunsetPredictionWidget()
}

/*
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = GlanceModifier.defaultWeight()
                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.logo_degrade),
                            contentDescription = "",
                            modifier = GlanceModifier.size(50.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.sunset)
                        )
                        Text(
                            text = "15:34"
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = GlanceModifier.defaultWeight()
                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.logo_degrade),
                            contentDescription = "",
                            modifier = GlanceModifier.size(50.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.golden_hour)
                        )
                        Text(
                            text = "15:34"
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = GlanceModifier.defaultWeight()
                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.logo_degrade),
                            contentDescription = "",
                            modifier = GlanceModifier.size(50.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.blue_hour)
                        )
                        Text(
                            text = "15:34"
                        )
                    }
                }
 */