package com.madteam.sunset.ui.widget.sunsetpredictionwidget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.madteam.sunset.R

class SmallSunsetHourWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            SmallSunsetHourContent()
        }
    }

    @Preview
    @Composable
    fun SmallSunsetHourContent() {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(Color(0xFFFDFDFD)))
                .cornerRadius(20.dp)
                .padding(4.dp)
        ) {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    ImageProvider(R.drawable.logo_degrade),
                    contentDescription = "Logo of sunset app",
                    modifier = GlanceModifier.size(40.dp)
                )
                Text(
                    text = "18:45",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp,
                        color = ColorProvider(Color(0xFFE86820))
                    )
                )
                Text(
                    text = "Mon, 23 Nov",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = ColorProvider(Color(0xFFE86820))
                    )
                )
            }
        }
    }

}

class SmallSunsetHourWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = SmallSunsetHourWidget()
}