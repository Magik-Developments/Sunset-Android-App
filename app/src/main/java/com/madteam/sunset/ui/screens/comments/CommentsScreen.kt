package com.madteam.sunset.ui.screens.comments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.madteam.sunset.R
import com.madteam.sunset.model.PostComment
import com.madteam.sunset.ui.common.ChatTextField
import com.madteam.sunset.ui.common.CustomDivider
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.GoBackTopAppBar
import com.madteam.sunset.ui.common.ProfileImage
import com.madteam.sunset.ui.theme.secondaryRegularBodyL
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM

@Composable
fun CommentsScreen(
    viewModel: CommentsViewModel = hiltViewModel(),
    commentsReference: String,
    navController: NavController
) {

    viewModel.setPostReference("posts/$commentsReference")
    val comments by viewModel.comments.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            GoBackTopAppBar(title = R.string.comments_title) {
                navController.popBackStack()
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CommentsContent(
                    comments = comments,
                    onCommentClick = viewModel::addNewComment
                )
            }
        }
    )
}

@Composable
fun CommentsContent(
    comments: List<PostComment>,
    onCommentClick: (String) -> Unit
) {

    var newCommentText by remember {
        mutableStateOf("")
    }


    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 70.dp)
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            itemsIndexed(comments.sortedBy {
                it.creation_date
            }) { _, comment ->
                ConstraintLayout(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                ) {
                    val (userImage, userUsername, creationDate, commentText) = createRefs()
                    ProfileImage(
                        imageUrl = comment.author.image,
                        size = 60.dp,
                        modifier = Modifier.constrainAs(userImage) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                        })
                    Text(
                        text = "@" + comment.author.username,
                        style = secondarySemiBoldBodyM,
                        modifier = Modifier.constrainAs(userUsername) {
                            start.linkTo(userImage.end, 8.dp)
                            top.linkTo(parent.top)
                        })
                    Text(
                        text = comment.creation_date,
                        style = secondaryRegularBodyM,
                        color = Color(0xFF999999),
                        modifier = Modifier.constrainAs(creationDate) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                        })
                    Text(
                        text = comment.comment,
                        style = secondaryRegularBodyL,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .constrainAs(commentText) {
                                top.linkTo(userUsername.bottom, 8.dp)
                                start.linkTo(userImage.end, 8.dp)
                            })
                }
                CustomSpacer(size = 8.dp)
                CustomDivider(modifier = Modifier.fillMaxWidth(), color = Color(0xFF999999))
            }
        }
    }
    Column(
        verticalArrangement = Arrangement.Bottom, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 8.dp)
    ) {
        ChatTextField(
            textValue = newCommentText,
            onValueChange = { newCommentText = it },
            onSendClick = {
                onCommentClick(newCommentText)
                newCommentText = ""
            })
        CustomSpacer(size = 8.dp)
    }
}

@Preview(showSystemUi = true)
@Composable
fun CommentsContentPreview() {
}