package com.madteam.sunset.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AirlineSeatReclineNormal
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.BeachAccess
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.DirectionsBoat
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.EvStation
import androidx.compose.material.icons.outlined.FamilyRestroom
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Forest
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Hiking
import androidx.compose.material.icons.outlined.Kayaking
import androidx.compose.material.icons.outlined.LocalBar
import androidx.compose.material.icons.outlined.LocalParking
import androidx.compose.material.icons.outlined.Motorcycle
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.PedalBike
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.SignalCellularAlt
import androidx.compose.material.icons.outlined.Tram
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.WbTwilight
import androidx.compose.material.icons.outlined.Wc
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.ui.screens.addreview.FAVORABLE_ATTRIBUTES
import com.madteam.sunset.ui.screens.addreview.NON_FAVORABLE_ATTRIBUTES
import com.madteam.sunset.ui.screens.addreview.SUNSET_ATTRIBUTES
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondaryRegularBodyS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyL
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM

val fieldToIconMap = mapOf(
    "Beach" to Icons.Outlined.BeachAccess,
    "City" to Icons.Outlined.Apartment,
    "Groups" to Icons.Outlined.Groups,
    "Parking" to Icons.Outlined.LocalParking,
    "Mountain" to Icons.Outlined.Forest,
    "Block" to Icons.Outlined.Block,
    "Wifi" to Icons.Outlined.Wifi,
    "Signal" to Icons.Outlined.SignalCellularAlt,
    "Food" to Icons.Outlined.Fastfood,
    "Drinks" to Icons.Outlined.LocalBar,
    "Walk" to Icons.Outlined.Hiking,
    "Car" to Icons.Outlined.DirectionsCar,
    "Boat" to Icons.Outlined.DirectionsBoat,
    "Bike" to Icons.Outlined.PedalBike,
    "Motorcycle" to Icons.Outlined.Motorcycle,
    "Tram" to Icons.Outlined.Tram,
    "Kayak" to Icons.Outlined.Kayaking,
    "Money" to Icons.Outlined.Payments,
    "Family" to Icons.Outlined.FamilyRestroom,
    "Seat" to Icons.Outlined.AirlineSeatReclineNormal,
    "Wc" to Icons.Outlined.Wc,
    "EvStation" to Icons.Outlined.EvStation,
    "HorizonSunset" to Icons.Outlined.WbTwilight,
    "Visibility" to Icons.Outlined.Visibility
)

@Composable
fun AttributesBigListRow(
    attributesList: List<SpotAttribute>,
    onAttributeClick: (SpotAttribute) -> Unit
) {
    LazyRow(
        modifier = Modifier,
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(attributesList) { _, attribute ->
            val icon = fieldToIconMap[attribute.icon]
            val tint: Color = when (attribute.type) {
                FAVORABLE_ATTRIBUTES -> {
                    Color(0xFF81c784)
                }

                NON_FAVORABLE_ATTRIBUTES -> {
                    Color(0xFFe57373)
                }

                SUNSET_ATTRIBUTES -> {
                    Color(0xFFFFE094)
                }

                else -> {
                    Color.Black
                }
            }
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onAttributeClick(attribute) }
                    .border(1.dp, Color(0xFF999999), RoundedCornerShape(20.dp))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = "",
                            modifier = Modifier.size(24.dp),
                            tint = tint
                        )
                    }
                    Text(
                        text = attribute.title,
                        style = secondaryRegularBodyS,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AttributesSmallListRow(
    attributesList: List<SpotAttribute>
) {
    LazyRow(
        modifier = Modifier.padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(attributesList) { _, attribute ->
            val icon = fieldToIconMap[attribute.icon] ?: Icons.Outlined.QuestionMark
            val tint: Color = when (attribute.type) {
                FAVORABLE_ATTRIBUTES -> {
                    Color(0xFF81c784)
                }

                NON_FAVORABLE_ATTRIBUTES -> {
                    Color(0xFFe57373)
                }

                SUNSET_ATTRIBUTES -> {
                    Color(0xFFFFE094)
                }

                else -> {
                    Color.Black
                }
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(
                        1.dp,
                        Color(0xFF999999),
                        RoundedCornerShape(8.dp)
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "",
                        modifier = Modifier.size(24.dp),
                        tint = tint
                    )
                }
            }
        }
    }
}

@Composable
fun AttributesBigListSelectable(
    attributesList: List<SpotAttribute>,
    selectedAttributes: List<SpotAttribute>,
    onAttributeClick: (SpotAttribute) -> Unit,
    filterAttributesBy: String
) {
    LazyRow(
        modifier = Modifier.padding(start = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(attributesList.filter { it.type == filterAttributesBy }) { _, attribute ->
            val isSelected = selectedAttributes.contains(attribute)
            val customBackgroundColor = if (isSelected) Color(0x80FFB600) else Color.White
            val icon = fieldToIconMap[attribute.icon]
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, Color(0xFF999999), RoundedCornerShape(20.dp))
                    .background(customBackgroundColor, RoundedCornerShape(20.dp))
                    .clickable { onAttributeClick(attribute) }

            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = attribute.title,
                        style = secondaryRegularBodyS,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FilterAttributesButton(
    filterOptions: List<SpotAttribute>,
    selectedOptions: List<SpotAttribute>,
    onOptionClicked: (SpotAttribute) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(filterOptions) { _, option ->
            val icon = fieldToIconMap[option.icon]
            val isSelected = selectedOptions.contains(option)
            val customBackgroundColor = if (isSelected) Color(0xFFFFE094) else Color.White
            val customBorderColor = if (isSelected) Color(0xFFFFB600) else Color(0xFF999999)
            val customTextColor = if (isSelected) Color.Black else Color(0xFF333333)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, customBorderColor, RoundedCornerShape(10.dp))
                    .background(customBackgroundColor, RoundedCornerShape(10.dp))
                    .clickable { onOptionClicked(option) }

            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (icon != null) {
                        androidx.compose.material.Icon(
                            imageVector = icon,
                            contentDescription = "",
                            modifier = Modifier
                                .size(16.dp),
                            tint = customTextColor
                        )
                    }
                    CustomSpacer(size = 4.dp)
                    androidx.compose.material.Text(
                        text = option.title,
                        style = secondarySemiBoldBodyM,
                        color = customTextColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }

}

@Composable
fun AttributeInfoDialog(
    attribute: SpotAttribute,
    setShowDialog: (Boolean) -> Unit
) {
    val icon = fieldToIconMap[attribute.icon]
    val tint: Color = when (attribute.type) {
        FAVORABLE_ATTRIBUTES -> {
            Color(0xFF81c784)
        }

        NON_FAVORABLE_ATTRIBUTES -> {
            Color(0xFFe57373)
        }

        SUNSET_ATTRIBUTES -> {
            Color(0xFFFFE094)
        }

        else -> {
            Color.Black
        }
    }
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = 2.dp
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Attribute icon",
                        tint = tint
                    )
                }
                CustomSpacer(size = 8.dp)
                Text(
                    text = attribute.title,
                    style = secondarySemiBoldBodyL,
                    textAlign = TextAlign.Center
                )
                CustomSpacer(size = 8.dp)
                Text(
                    text = attribute.description,
                    style = secondaryRegularBodyM,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
