package com.madteam.sunset.ui.screens.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.madteam.sunset.ui.common.SelectedCommentTopAppBar
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
    val selectedComment by viewModel.selectedComment.collectAsStateWithLifecycle()



    Scaffold(
        topBar = {
            if (selectedComment == PostComment()) {
                GoBackTopAppBar(title = R.string.comments_title) {
                    navController.popBackStack()
                }
            } else {
                SelectedCommentTopAppBar(
                    title = R.string.selected_comment_title,
                    onQuitClick = {
                        viewModel.unSelectComment()
                    },
                    onDeleteClick = {
                        viewModel.deleteSelectedComment()
                    },
                    isCommentAuthor = viewModel.checkIfUserIsCommentAuthor()
                )
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CommentsContent(
                    comments = comments,
                    onCommentClick = viewModel::addNewComment,
                    selectedComment = selectedComment,
                    onSelectedComment = viewModel::onSelectedComment
                )
            }
        }
    )
}

@Composable
fun CommentsContent(
    comments: List<PostComment>,
    onCommentClick: (String) -> Unit,
    selectedComment: PostComment,
    onSelectedComment: (PostComment) -> Unit
) {

    var newCommentText by remember {
        mutableStateOf("")
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(bottom = 70.dp)
    ) {
        LazyColumn {
            itemsIndexed(comments.sortedBy {
                it.creation_date
            }) { _, comment ->
                val isSelected = comment == selectedComment
                val backgroundColor = if (isSelected) Color(0xCDFFB600) else Color.White
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .clickable {
                            onSelectedComment(comment)
                        }
                ) {
                    val (userImage, userUsername, creationDate, commentText) = createRefs()
                    ProfileImage(
                        imageUrl = comment.author.image,
                        size = 60.dp,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .constrainAs(userImage) {
                                start.linkTo(parent.start, 16.dp)
                                top.linkTo(parent.top, 16.dp)
                            })
                    Text(
                        text = "@" + comment.author.username,
                        style = secondarySemiBoldBodyM,
                        modifier = Modifier.constrainAs(userUsername) {
                            start.linkTo(userImage.end, 8.dp)
                            top.linkTo(parent.top, 16.dp)
                        })
                    Text(
                        text = comment.creation_date,
                        style = secondaryRegularBodyM,
                        color = Color(0xFF999999),
                        modifier = Modifier.constrainAs(creationDate) {
                            end.linkTo(parent.end, 24.dp)
                            top.linkTo(parent.top, 16.dp)
                        })
                    Text(
                        text = comment.comment,
                        style = secondaryRegularBodyL,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .padding(end = 8.dp, bottom = 16.dp)
                            .constrainAs(commentText) {
                                top.linkTo(userUsername.bottom, 8.dp)
                                start.linkTo(userImage.end, 8.dp)
                            })
                    CustomDivider(modifier = Modifier.fillMaxWidth(), color = Color(0xFF999999))
                }
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