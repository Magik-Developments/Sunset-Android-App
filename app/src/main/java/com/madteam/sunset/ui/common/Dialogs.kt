package com.madteam.sunset.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
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
import com.madteam.sunset.ui.theme.primaryBoldHeadlineS
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyL

@Composable
fun DismissAndPositiveDialog(
    setShowDialog: (Boolean) -> Unit,
    @StringRes dialogTitle: Int,
    @StringRes dialogDescription: Int,
    @StringRes positiveButtonText: Int,
    @StringRes dismissButtonText: Int,
    image: Int? = null,
    dismissClickedAction: () -> Unit,
    positiveClickedAction: () -> Unit
) {
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = 2.dp,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Column {
                if (image != null) {
                    Image(
                        modifier = Modifier
                            .width(140.dp)
                            .height(140.dp)
                            .align(alignment = Alignment.CenterHorizontally),
                        painter = painterResource(id = image),
                        contentDescription = "Dialog image",
                        alignment = Alignment.Center
                    )
                } else {
                    CustomSpacer(size = 16.dp)
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 24.dp),
                        text = stringResource(dialogTitle),
                        style = primaryBoldHeadlineS,
                        textAlign = TextAlign.Center
                    )
                }
                CustomSpacer(size = 24.dp)
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = stringResource(dialogDescription),
                    style = secondaryRegularBodyL,
                    textAlign = TextAlign.Center
                )
                CustomSpacer(size = 24.dp)
                Row(Modifier.fillMaxWidth()) {
                    Button(
                        onClick = dismissClickedAction,
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFE094)),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Text(
                            text = stringResource(dismissButtonText),
                            style = secondarySemiBoldBodyL,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                    Button(
                        onClick = positiveClickedAction,
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Text(
                            text = stringResource(positiveButtonText),
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

@Composable
fun CircularLoadingDialog(
) {
    Box(
        Modifier
            .size(100.dp)
            .background(color = Color.White, RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color(0xFFFFB600))
    }
}
