package com.madteam.sunset.ui.screens.discover

import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.madteam.sunset.model.SpotClusterItem
import com.madteam.sunset.ui.common.RoundedCloseIconButton
import com.madteam.sunset.ui.theme.primaryBoldHeadlineS
import com.madteam.sunset.ui.theme.secondaryRegularBodyL

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SpotClusterInfo(selectedCluster: SpotClusterItem, onClose: (SpotClusterItem) -> Unit, onItemClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp).clickable { onItemClicked() },
        backgroundColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        elevation = 2.dp,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box {
                GlideImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.4f),
                    model = "https://t4.ftcdn.net/jpg/01/04/78/75/360_F_104787586_63vz1PkylLEfSfZ08dqTnqJqlqdq0eXx.jpg",
                    contentDescription = "Image of Sunset Spot",
                    contentScale = ContentScale.Crop
                )
                Box(modifier = Modifier.padding(8.dp)) {
                    RoundedCloseIconButton { onClose(selectedCluster) }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                if (selectedCluster != null) {
                    Text(text = selectedCluster.title, style = primaryBoldHeadlineS)
                }
                Text(text = "Lorem ipsum", style = secondaryRegularBodyL)
            }
        }
    }
}
