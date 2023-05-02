package com.madteam.sunset.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Companion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.madteam.sunset.R
import com.madteam.sunset.R.string
import com.madteam.sunset.ui.theme.primaryBoldHeadlineS
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyL

@Composable
fun GDPRDialog(
    setShowDialog: (Boolean) -> Unit,
    readPoliciesClicked: () -> Unit,
    acceptPoliciesClicked: () -> Unit
) {
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = 2.dp,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Column() {
                Image(
                    modifier = Modifier
                        .width(140.dp)
                        .height(140.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.sunset_vectorial_art_01),
                    contentDescription = stringResource(string.sunset_art_image_description),
                    alignment = Alignment.Center
                )
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = stringResource(string.privacy_dialog_title),
                    style = primaryBoldHeadlineS,
                    textAlign = TextAlign.Center
                )
                CustomSpacer(size = 24.dp)
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = stringResource(string.privacy_dialog_description),
                    style = secondaryRegularBodyL,
                    textAlign = TextAlign.Center
                )
                CustomSpacer(size = 24.dp)
                Row(Modifier.fillMaxWidth()) {
                    Button(
                        onClick = readPoliciesClicked,
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFE094)),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Text(
                            text = stringResource(string.read_policies),
                            style = secondarySemiBoldBodyL,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                    Button(
                        onClick = acceptPoliciesClicked,
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Text(
                            text = stringResource(string.sign_up),
                            style = secondarySemiBoldBodyL,
                            color = Color.White,
                            textAlign = Companion.Center
                        )
                    }
                }
            }
        }
    }
}
