package com.madteam.sunset.repositories

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.madteam.sunset.model.PostComment
import com.madteam.sunset.model.Spot
import com.madteam.sunset.model.SpotAttribute
import com.madteam.sunset.model.SpotClusterItem
import com.madteam.sunset.model.SpotPost
import com.madteam.sunset.model.SpotReview
import com.madteam.sunset.model.UserProfile
import com.madteam.sunset.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

private const val USERS_COLLECTION_PATH = "users"
private const val SPOTS_LOCATIONS_COLLECTION_PATH = "spots_locations"
private const val SPOT_REVIEWS_COLLECTION = "spot_reviews"
private const val SPOTS_COLLECTION_PATH = "spots"
private const val POSTS_COLLECTION_PATH = "posts"
private const val COMMENTS_POST_COLLECTION_PATH = "comments"
private const val LIKED_BY_POST_COLLECTION_PATH = "liked_by"
private const val IMAGES_STORAGE_POSTS_PATH = "posts_images/"

class DatabaseRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) : DatabaseContract {

    override fun createUser(
        email: String,
        username: String,
        provider: String
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())

        val userDocument = firebaseFirestore.collection(USERS_COLLECTION_PATH).document(username)
        val documentSnapshot = userDocument.get().await()
        if (documentSnapshot.exists()) {
            emit(Resource.Error("e_user_already_exists"))
            return@flow
        }

        val currentDate = Calendar.getInstance().time.toString()
        val user = hashMapOf(
            "username" to username,
            "email" to email,
            "provider" to provider,
            "creation_date" to currentDate
        )

        firebaseFirestore.collection(USERS_COLLECTION_PATH).document(username).set(user).await()
        emit(Resource.Success("User database has been created"))
    }.catch { exception ->
        emit(Resource.Error(exception.message.toString()))
    }

    override fun createPostComment(
        comment: PostComment,
        postDocument: String
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        val currentDate = Calendar.getInstance().time.toString()
        val commentsReference =
            firebaseFirestore.document(postDocument).collection(
                COMMENTS_POST_COLLECTION_PATH
            )
        val authorReference = firebaseFirestore
            .collection(USERS_COLLECTION_PATH)
            .document(comment.author.username)

        println("authorReference: " + authorReference.path)
        val newCommentDocument = commentsReference.document()

        val newCommentData = hashMapOf(
            "id" to newCommentDocument.id,
            "author" to authorReference,
            "comment" to comment.comment,
            "creation_date" to currentDate
        )

        newCommentDocument.set(newCommentData).await()
        emit(Resource.Success("Comment has been created"))
    }.catch { exception ->
        emit(Resource.Error(exception.message.toString()))
        Log.e("DatabaseRepository::createPostComment", "Error: ${exception.message}")
    }

    override fun deletePostComment(
        comment: PostComment,
        postDocument: String
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())

        val commentsReference =
            firebaseFirestore.document(postDocument)
                .collection(COMMENTS_POST_COLLECTION_PATH)

        val commentDocument = commentsReference.document(comment.id)

        commentDocument.delete().await()

        emit(Resource.Success("Comment has been deleted"))
    }.catch { exception ->
        emit(Resource.Error(exception.message.toString()))
        Log.e("DatabaseRepository::deletePostComment", "Error: ${exception.message}")
    }

    override fun modifyUserPostLike(
        postReference: String,
        username: String
    ): Flow<Resource<String>> = flow<Resource<String>> {

        val likedByReference =
            firebaseFirestore.document(postReference)
                .collection(LIKED_BY_POST_COLLECTION_PATH)

        val userLikeDocumentSnapshot =
            likedByReference.document(username).get().await()

        if (userLikeDocumentSnapshot.exists()) {
            likedByReference.document(username).delete()
            firebaseFirestore.document(postReference).update(
                "likes", FieldValue.increment(-1)
            ).await()
        } else {
            likedByReference.document(username).set(
                hashMapOf(
                    "date" to Calendar.getInstance().time.toString()
                )
            ).await()
            firebaseFirestore.document(postReference).update(
                "likes", FieldValue.increment(1)
            ).await()
        }
    }.catch { exception ->
        emit(Resource.Error(exception.message.toString()))
        Log.e("DatabaseRepository::modifyUserPostLike", "Error: ${exception.message}")
    }

    override fun checkIfPostIsLikedByUser(
        postReference: String,
        username: String
    ): Flow<Resource<Boolean>> = flow {

        val likedByReference =
            firebaseFirestore.document(postReference)
                .collection(LIKED_BY_POST_COLLECTION_PATH)

        val userLikeDocumentSnapshot =
            likedByReference.document(username).get().await()

        val postIsLikedByUser = userLikeDocumentSnapshot.exists()
        emit(Resource.Success(postIsLikedByUser))

    }.catch { exception ->
        Log.e("DatabaseRepository::modifyUserPostLike", "Error: ${exception.message}")
    }

    override fun getUserByEmail(email: String, userProfileCallback: (UserProfile) -> Unit) {
        firebaseFirestore.collection(USERS_COLLECTION_PATH).whereEqualTo("email", email)
            .limit(1)
            .get()
            .addOnSuccessListener { userDocument ->
                userProfileCallback(userDocument.toObjects(UserProfile::class.java)[0])
            }
    }

    override fun updateUser(user: UserProfile): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        val userDocument =
            firebaseFirestore.collection(USERS_COLLECTION_PATH).document(user.username)
        val documentSnapshot = userDocument.get().await()
        if (!documentSnapshot.exists()) {
            emit(Resource.Error("e_user_database_not_found"))
            return@flow
        }
        val updateMap = HashMap<String, Any?>()
        updateMap["name"] = user.name
        updateMap["location"] = user.location
        firebaseFirestore.collection(USERS_COLLECTION_PATH).document(user.username)
            .update(updateMap)
            .await()
        emit(Resource.Success("User database has been updated"))
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }

    override fun getSpotsLocations(): Flow<List<SpotClusterItem>> = flow {
        try {
            val spotCollection =
                firebaseFirestore.collection(SPOTS_LOCATIONS_COLLECTION_PATH).get().await()
            val spotList = spotCollection.documents.mapNotNull { document ->
                val id = document.id
                val name = document.getString("name")
                val location = document.getGeoPoint("location")
                val spot = document.getDocumentReference("spot")

                if (name != null && location != null && spot != null) {
                    SpotClusterItem(
                        id = id,
                        name = name,
                        spot = spot,
                        location = location,
                        isSelected = false
                    )
                } else {
                    null
                }
            }
            emit(spotList)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override fun getSpotByDocRef(docRef: String): Flow<Spot> = flow {
        val documentReference = firebaseFirestore.document(docRef)
        val documentSnapshot = documentReference.get().await()

        if (documentSnapshot.exists()) {
            val id = documentSnapshot.id
            val creationDate = documentSnapshot.getString("creation_date")
            val featuredImages = documentSnapshot.get("featured_images") as List<String>
            val name = documentSnapshot.getString("name")
            val description = documentSnapshot.getString("description")
            val score = documentSnapshot.getDouble("score")
            val visitedTimes = documentSnapshot.get("visited_times")
            val likes = documentSnapshot.get("likes")
            val locationInLatLng = documentSnapshot.getGeoPoint("location_in_latlng")
            val location = documentSnapshot.getString("location")
            val spottedByDocRef = documentSnapshot.getDocumentReference("spotted_by")
            val attributeDocRefs = documentSnapshot.get("attributes") as List<DocumentReference>
            var spottedBy = UserProfile()
            var spotAttributes = listOf<SpotAttribute>()
            val spotReviewsRef = documentReference.collection("spot_reviews")
            var spotReviews = listOf<SpotReview>()
            val spotPostsRefs = documentSnapshot.get("posts") as List<DocumentReference>
            var spotPosts = listOf<SpotPost>()
            if (spottedByDocRef != null) {
                getUserProfileByDocRef(spottedByDocRef).collectLatest { profile ->
                    spottedBy = profile
                }
            }
            getSpotAttributesByDocRefs(attributeDocRefs).collectLatest { attributes ->
                spotAttributes = attributes
            }
            getSpotReviewsByCollectionRef(spotReviewsRef).collectLatest { reviews ->
                spotReviews = reviews
            }
            getSpotPostsByDocRefs(spotPostsRefs).collectLatest { posts ->
                spotPosts = posts
            }

            val spotData = Spot(
                id = id,
                spottedBy = spottedBy,
                featuredImages = featuredImages,
                creationDate = creationDate ?: "",
                name = name ?: "",
                description = description ?: "",
                score = score?.toFloat() ?: 0.0f,
                visitedTimes = visitedTimes.toString().toIntOrNull() ?: 0,
                likes = likes.toString().toIntOrNull() ?: 0,
                locationInLatLng = locationInLatLng ?: GeoPoint(0.0, 0.0),
                location = location ?: "",
                attributes = spotAttributes,
                spotReviews = spotReviews,
                spotPosts = spotPosts
            )
            emit(spotData)
        } else {
            emit(Spot())
        }
    }.catch { exception ->
        Log.e("DatabaseRepository::getSpotByDocRef", "Error: ${exception.message}")
        emit(Spot())
    }

    override fun getUserProfileByDocRef(docRef: DocumentReference): Flow<UserProfile> = flow {
        val documentReference = firebaseFirestore.document(docRef.path)
        val userSnapshot = documentReference.get().await()

        if (userSnapshot.exists()) {
            val username = userSnapshot.getString("username")
            val email = userSnapshot.getString("email")
            val provider = userSnapshot.getString("provider")
            val location = userSnapshot.getString("location")
            val creationDate = userSnapshot.getString("creation_date")
            val userImage = userSnapshot.getString("image")
            val usernameName = userSnapshot.getString("name")
            val userProfile = UserProfile(
                username = username ?: "",
                email = email ?: "",
                provider = provider ?: "",
                creation_date = creationDate ?: "",
                name = usernameName ?: "",
                location = location ?: "",
                image = userImage ?: ""
            )
            emit(userProfile)
        }
    }.catch { exception ->
        Log.e("DatabaseRepository::getUserProfileByDocRef", "Error: ${exception.message}")
        emit(UserProfile())
    }

    override fun getSpotAttributesByDocRefs(docRefs: List<DocumentReference>): Flow<List<SpotAttribute>> =
        flow {
            val attributesList = mutableListOf<SpotAttribute>()
            for (attributeRef in docRefs) {
                val attributeSnapshot = attributeRef.get().await()
                val id = attributeSnapshot.id
                val title = attributeSnapshot.getString("title")
                val description = attributeSnapshot.getString("description")
                val icon = attributeSnapshot.getString("icon")
                val favorable = attributeSnapshot.getBoolean("favorable")
                if (description != null && title != null && icon != null && favorable != null) {
                    val attributeData = SpotAttribute(
                        id,
                        description,
                        title,
                        icon,
                        favorable
                    )
                    attributesList.add(attributeData)
                }
            }
            emit(attributesList)
        }.catch { exception ->
            Log.e("DatabaseRepository::getSpotAttributesByDocRefs", "Error: ${exception.message}")
            emit(mutableListOf())
        }

    override fun getSpotReviewsByCollectionRef(collectionRef: CollectionReference): Flow<List<SpotReview>> =
        flow<List<SpotReview>> {
            val spotReviewsList = mutableListOf<SpotReview>()
            collectionRef.get().await().forEach { spotReviewSnapshot ->
                val id = spotReviewSnapshot.id
                val description = spotReviewSnapshot.getString("description")
                val title = spotReviewSnapshot.getString("title")
                val postedByDocRef = spotReviewSnapshot.getDocumentReference("posted_by")
                var postedBy = UserProfile()
                if (postedByDocRef != null) {
                    getUserProfileByDocRef(postedByDocRef).collectLatest { profile ->
                        postedBy = profile
                    }
                }
                val reviewAttributesRefs =
                    spotReviewSnapshot.get("spot_attr") as List<DocumentReference>
                var reviewAttributes = listOf<SpotAttribute>()
                getSpotAttributesByDocRefs(reviewAttributesRefs).collectLatest { attributes ->
                    reviewAttributes = attributes
                }
                val creationDate = spotReviewSnapshot.getString("creation_date")
                val score = spotReviewSnapshot.getDouble("score")
                if (description != null && title != null && creationDate != null && score != null) {
                    spotReviewsList.add(
                        SpotReview(
                            id,
                            description,
                            title,
                            postedBy,
                            reviewAttributes,
                            creationDate,
                            score.toFloat()
                        )
                    )
                }
            }
            emit(spotReviewsList)
        }.catch { exception ->
            Log.e("DatabaseRepository::getSpotReviewsByDocRef", "Error: ${exception.message}")
            emit(mutableListOf())
        }

    override fun getSpotPostsByDocRefs(docRefs: List<DocumentReference>): Flow<List<SpotPost>> =
        flow<List<SpotPost>> {
            val postsList = mutableListOf<SpotPost>()
            for (postRef in docRefs) {
                val postSnapshot = postRef.get().await()
                val id = postSnapshot.id
                val authorRef = postSnapshot.getDocumentReference("author")
                var author = UserProfile()
                if (authorRef != null) {
                    getUserProfileByDocRef(authorRef).collectLatest { profile ->
                        author = profile
                    }
                }
                var commentsList = listOf<PostComment>()
                getCommentsFromPostRef(postRef.path).collectLatest { comments ->
                    commentsList = comments
                }
                val description = postSnapshot.getString("description")
                val creationDate = postSnapshot.getString("creation_date")
                val images = postSnapshot.get("images") as List<String>
                val spotRef = postSnapshot.getDocumentReference("spot")?.path
                val likes = postSnapshot.get("likes")
                val likedBySnapshot = postRef.collection("liked_by").get().await()
                val likedByList = likedBySnapshot.documents.map { doc -> doc.id }
                if (description != null && creationDate != null && spotRef != null) {
                    val spotPost = SpotPost(
                        id,
                        description,
                        spotRef,
                        images,
                        author,
                        creationDate,
                        commentsList,
                        likedByList,
                        likes.toString().toInt()
                    )
                    postsList.add(spotPost)
                }
            }
            emit(postsList)
        }.catch { exception ->
            Log.e("DatabaseRepository::getSpotPostsByDocRefs", "Error: ${exception.message}")
            emit(mutableListOf())
        }

    override fun getSpotPostByDocRef(docRef: String): Flow<SpotPost> =
        flow {
            val documentReference = firebaseFirestore.document(docRef)
            val postSnapshot = documentReference.get().await()
            val id = postSnapshot.id
            val authorRef = postSnapshot.getDocumentReference("author")
            var author = UserProfile()
            if (authorRef != null) {
                getUserProfileByDocRef(authorRef).collectLatest { profile ->
                    author = profile
                }
            }
            var commentsList = listOf<PostComment>()
            getCommentsFromPostRef(documentReference.path).collectLatest { comments ->
                commentsList = comments
            }
            val description = postSnapshot.getString("description")
            val creationDate = postSnapshot.getString("creation_date")
            val images = postSnapshot.get("images") as List<String>
            val spotRef = postSnapshot.getDocumentReference("spot")?.path
            val likes = postSnapshot.get("likes")
            val likedBySnapshot = documentReference.collection("liked_by").get().await()
            val likedByList = likedBySnapshot.documents.map { doc -> doc.id }
            if (description != null && creationDate != null && spotRef != null) {
                val spotPost = SpotPost(
                    id,
                    description,
                    spotRef,
                    images,
                    author,
                    creationDate,
                    commentsList,
                    likedByList,
                    likes.toString().toInt()
                )
                emit(spotPost)
            }

        }.catch { exception ->
            Log.e("DatabaseRepository::getSpotPostByDocRef", "Error: ${exception.message}")
            emit(SpotPost())
        }

    override fun getCommentsFromPostRef(postRef: String): Flow<List<PostComment>> =
        flow {
            val commentsList = mutableListOf<PostComment>()
            val commentsSnapshot =
                firebaseFirestore.document(postRef).collection("comments").get().await()
            for (commentDoc in commentsSnapshot.documents) {
                val id = commentDoc.id
                val commentText = commentDoc.getString("comment")
                val creationDate = commentDoc.getString("creation_date")
                val authorRef = commentDoc.getDocumentReference("author")
                var author = UserProfile()
                if (authorRef != null) {
                    getUserProfileByDocRef(authorRef).collectLatest { profile ->
                        author = profile
                    }
                }
                if (commentText != null && creationDate != null) {
                    val comment = PostComment(id, commentText, author, creationDate)
                    commentsList.add(comment)
                }
            }
            emit(commentsList)
        }.catch { exception ->
            Log.e("DatabaseRepository::getCommentsFromPostRef", "Error: ${exception.message}")
            emit(mutableListOf())
        }

    override fun createSpotPost(
        spotRef: String,
        description: String?,
        imagesUriList: List<Uri>,
        authorUsername: String
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        val newPostDocument = firebaseFirestore.collection(POSTS_COLLECTION_PATH).document()
        val spotDocumentRef = firebaseFirestore.collection(SPOTS_COLLECTION_PATH).document(spotRef)
        val authorRef = firebaseFirestore.collection(USERS_COLLECTION_PATH).document(authorUsername)
        var imagesList = listOf<String>()
        uploadImages(
            imagesUriList,
            "$IMAGES_STORAGE_POSTS_PATH${newPostDocument.id}/"
        ).collectLatest { urlImagesList ->
            imagesList = urlImagesList
        }
        val newPost = hashMapOf(
            "id" to newPostDocument.id,
            "description" to description,
            "spot" to spotDocumentRef,
            "images" to imagesList,
            "author" to authorRef,
            "creation_date" to Calendar.getInstance().time.toString(),
            "likes" to 0
        )

        newPostDocument.set(newPost).await()
        spotDocumentRef.update("posts", FieldValue.arrayUnion(newPostDocument))
        emit(Resource.Success(newPostDocument.id))
    }.catch { exception ->
        Log.e("DatabaseRepository::createSpotPost", "Error: ${exception.message}")
        emit(Resource.Success("Error: " + exception.message.toString()))
    }

    override fun uploadImages(
        uriImagesList: List<Uri>,
        storagePath: String
    ): Flow<List<String>> = flow {

        val downloadUrls = mutableListOf<String>()

        val storageReference = firebaseStorage.getReference(storagePath)

        uriImagesList.forEach { uri ->
            val imageFileName = UUID.randomUUID().toString()
            val imageRef = storageReference.child(imageFileName)
            val uploadTask = imageRef.putFile(uri)
            val taskSnapshot = uploadTask.await()
            val downloadUrl = taskSnapshot.storage.downloadUrl.await()
            downloadUrls.add(downloadUrl.toString())
        }

        emit(downloadUrls)
    }.catch { exception ->
        Log.e("DatabaseRepository::uploadImages", "Error: ${exception.message}")
        emit(mutableListOf())
    }

    override fun getSpotReviewByDocRef(
        spotReference: String,
        docReference: String
    ): Flow<SpotReview> = flow {
        var review: SpotReview = SpotReview()
        val reviewSnapshot =
            firebaseFirestore.collection(SPOTS_COLLECTION_PATH).document(spotReference)
                .collection(SPOT_REVIEWS_COLLECTION).document(docReference).get().await()
        val description = reviewSnapshot.getString("description")
        val title = reviewSnapshot.getString("title")
        val postedByDocRef = reviewSnapshot.getDocumentReference("posted_by")
        var postedBy = UserProfile()
        if (postedByDocRef != null) {
            getUserProfileByDocRef(postedByDocRef).collectLatest { profile ->
                postedBy = profile
            }
        }
        val reviewAttributesRefs =
            reviewSnapshot.get("spot_attr") as List<DocumentReference>
        var reviewAttributes = listOf<SpotAttribute>()
        getSpotAttributesByDocRefs(reviewAttributesRefs).collectLatest { attributes ->
            reviewAttributes = attributes
        }
        val creationDate = reviewSnapshot.getString("creation_date")
        val score = reviewSnapshot.getDouble("score")

        if (description != null && title != null && creationDate != null && score != null) {
            review = SpotReview(
                id = reviewSnapshot.id,
                description = description,
                title = title,
                postedBy = postedBy,
                spotAttributes = reviewAttributes,
                creationDate = creationDate,
                score = score.toFloat()
            )
        }
        emit(review)
    }
}

interface DatabaseContract {

    fun createUser(email: String, username: String, provider: String): Flow<Resource<String>>
    fun getUserByEmail(email: String, userProfileCallback: (UserProfile) -> Unit)
    fun updateUser(user: UserProfile): Flow<Resource<String>>
    fun getSpotsLocations(): Flow<List<SpotClusterItem>>
    fun getSpotByDocRef(docRef: String): Flow<Spot>
    fun getUserProfileByDocRef(docRef: DocumentReference): Flow<UserProfile>
    fun getSpotAttributesByDocRefs(docRefs: List<DocumentReference>): Flow<List<SpotAttribute>>
    fun getSpotReviewsByCollectionRef(collectionRef: CollectionReference): Flow<List<SpotReview>>
    fun getSpotPostsByDocRefs(docRefs: List<DocumentReference>): Flow<List<SpotPost>>
    fun getSpotPostByDocRef(docRef: String): Flow<SpotPost>
    fun getCommentsFromPostRef(postRef: String): Flow<List<PostComment>>
    fun createSpotPost(
        spotRef: String,
        description: String?,
        imagesUriList: List<Uri>,
        authorUsername: String
    ): Flow<Resource<String>>

    fun uploadImages(uriImagesList: List<Uri>, storagePath: String): Flow<List<String>>
    fun createPostComment(comment: PostComment, postDocument: String): Flow<Resource<String>>
    fun deletePostComment(comment: PostComment, postDocument: String): Flow<Resource<String>>
    fun modifyUserPostLike(postReference: String, username: String): Flow<Resource<String>>
    fun checkIfPostIsLikedByUser(postReference: String, username: String): Flow<Resource<Boolean>>
    fun getSpotReviewByDocRef(spotReference: String, docReference: String): Flow<SpotReview>

}
