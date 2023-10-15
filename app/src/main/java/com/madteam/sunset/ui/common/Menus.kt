package com.madteam.sunset.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.madteam.sunset.R
import com.madteam.sunset.navigation.SunsetBottomNavItem
import com.madteam.sunset.ui.theme.primaryBoldHeadlineM
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.utils.shadow

@Composable
fun SunsetBottomNavigation(navController: NavController) {

    val unselectedContentColor = Color(0xB3FFB600)
    val selectedContentColor = Color(0xFF000000)

    val items = listOf(
        SunsetBottomNavItem.Home,
        SunsetBottomNavItem.SunsetPrediction,
        SunsetBottomNavItem.Discover,
        SunsetBottomNavItem.Profile
    )

    BottomNavigation(
        modifier = Modifier
            .height(84.dp)
            .shadow(
                color = Color(0x33000000),
                blurRadius = 2.dp,
                offsetY = (-2).dp
            )
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
        backgroundColor = Color.White
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon, contentDescription = item.title,
                        modifier = Modifier
                            .height(32.dp)
                            .width(32.dp)
                    )
                },
                unselectedContentColor = unselectedContentColor,
                selectedContentColor = selectedContentColor
            )
        }
    }
}

@Composable
fun GoBackTopAppBar(
    @StringRes title: Int,
    onClick: () -> Unit
) {
    TopAppBar(
        backgroundColor = Color.White,
        navigationIcon = {
            IconButton(onClick = { onClick() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go back")
            }
        },
        title = { Text(text = stringResource(id = title)) }
    )
}

@Composable
fun GoBackVariantTitleTopAppBar(
    title: String,
    onClick: () -> Unit
) {
    TopAppBar(
        backgroundColor = Color.White,
        navigationIcon = {
            IconButton(onClick = { onClick() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go back")
            }
        },
        title = { Text(text = title) }
    )
}

@Composable
fun SelectedCommentTopAppBar(
    @StringRes title: Int,
    onQuitClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isCommentAuthor: Boolean
) {
    TopAppBar(
        backgroundColor = Color(0xFFFFB600),
        navigationIcon = {
            IconButton(onClick = { onQuitClick() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Unselect",
                    tint = Color.White
                )
            }
        },
        actions = {
            if (isCommentAuthor) {
                IconButton(onClick = { onDeleteClick() }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete comment",
                        tint = Color.White
                    )
                }
            }
        },
        title = { Text(text = stringResource(id = title), color = Color.White) }
    )
}

@Composable
fun GoForwardTopAppBar(
    @StringRes title: Int,
    onQuitClick: () -> Unit,
    onContinueClick: () -> Unit,
    canContinue: Boolean
) {
    TopAppBar(
        backgroundColor = Color.White,
        navigationIcon = {
            IconButton(onClick = { onQuitClick() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Unselect",
                    tint = Color.Black
                )
            }
        },
        actions = {
            if (canContinue) {
                IconButton(onClick = { onContinueClick() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Continue",
                        tint = Color(0xFFFFB600)
                    )
                }
            }
        },
        title = { Text(text = stringResource(id = title), color = Color.Black) }
    )
}

@Composable
fun MyProfileTopAppBar(
    username: String,
    openMenuClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = username, style = primaryBoldHeadlineM) },
        backgroundColor = Color.White,
        actions = {
            IconButton(onClick = { openMenuClick() }) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Open menu button"
                )
            }
        }
    )
}

@Composable
fun BottomSheetSettingsMenu(
    isUserAdmin: Boolean,
    onReportsClick: () -> Unit,
    onLogOutClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CustomSpacer(size = 8.dp)
            CardHandler(modifier = Modifier.align(Alignment.CenterHorizontally))

            //Reports screen [admins only]
            if (isUserAdmin) {
                SettingsMenuItem(
                    icon = Icons.Filled.Flag,
                    onClick = { onReportsClick() },
                    text = R.string.reports
                )
            }

            //Add more options

            //Log out option (Always the last option)
            SettingsMenuItem(
                icon = Icons.Filled.Logout,
                tint = Color(0xFFFF4444),
                onClick = { onLogOutClick() },
                text = R.string.log_out
            )
        }
    }
}

@Composable
fun SettingsMenuItem(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    icon: ImageVector,
    @StringRes text: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomSpacer(size = 24.dp)
        androidx.compose.material3.Icon(
            imageVector = icon,
            contentDescription = "Item button",
            tint = tint
        )
        CustomSpacer(size = 16.dp)
        Text(
            text = stringResource(id = text),
            style = secondaryRegularBodyL,
            color = tint,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        CustomSpacer(size = 24.dp)
    }
}

@Preview
@Composable
fun MyProfileTopAppBarPreview() {
    MyProfileTopAppBar(
        username = "adriaa12",
        openMenuClick = {}
    )
}

