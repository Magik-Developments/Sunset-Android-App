package com.madteam.sunset.repositories

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.madteam.sunset.model.PostComment
import com.madteam.sunset.model.Report
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
private const val SPOT_REVIEWS_COLLECTION = "spot_reviews"
private const val SPOT_ATTRIBUTES_COLLECTION = "spot_attributes"
private const val SPOTS_COLLECTION_PATH = "spots"
private const val POSTS_COLLECTION_PATH = "posts"
private const val REPORTS_COLLECTION_PATH = "reports"
private const val REPORT_OPTIONS_SPOT_COLLECTION_PATH = "reports/report_options/spot_report_options"
private const val COMMENTS_POST_COLLECTION_PATH = "comments"
private const val LIKED_BY_POST_COLLECTION_PATH = "liked_by"
private const val LIKED_BY_SPOT_COLLECTION_PATH = "liked_by"
private const val IMAGES_STORAGE_POSTS_PATH = "posts_images/"
private const val IMAGES_STORAGE_SPOTS_PATH = "spots_images/"
private const val IMAGES_STORAGE_PROFILE_IMAGES_PATH = "profile_images/"

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

        val imageStoragePath = "profile_images/default_images/${username.first()}.png"
        val imageReference = firebaseStorage.getReference(imageStoragePath).downloadUrl.await()

        val currentDate = Calendar.getInstance().time.toString()
        val user = hashMapOf(
            "username" to username,
            "email" to email,
            "provider" to provider,
            "creation_date" to currentDate,
            "image" to imageReference.toString()
        )

        firebaseFirestore.collection(USERS_COLLECTION_PATH).document(username).set(user).await()
        emit(Resource.Success("User database has been created"))
    }.catch { exception ->
        Log.e("DatabaseRepository::createUser", "Error: ${exception.stackTrace}")
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

    override fun modifyUserSpotLike(
        spotReference: String,
        username: String
    ): Flow<Resource<String>> = flow<Resource<String>> {
        val likedByReference =
            firebaseFirestore.document(spotReference)
                .collection(LIKED_BY_SPOT_COLLECTION_PATH)
        val userLikeDocumentSnapshot =
            likedByReference.document(username).get().await()
        if (userLikeDocumentSnapshot.exists()) {
            likedByReference.document(username).delete()
            firebaseFirestore.document(spotReference).update(
                "likes", FieldValue.increment(-1)
            ).await()
        } else {
            likedByReference.document(username).set(
                hashMapOf(
                    "date" to Calendar.getInstance().time.toString()
                )
            ).await()
            firebaseFirestore.document(spotReference).update(
                "likes", FieldValue.increment(1)
            ).await()
        }
    }.catch { exception ->
        emit(Resource.Error(exception.message.toString()))
        Log.e("DatabaseRepository::modifyUserSpotLike", "Error: ${exception.message.toString()}")
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
        var profileImage = ""
        if (user.image.isNotBlank()) {
            uploadImages(
                listOf(user.image.toUri()),
                "$IMAGES_STORAGE_PROFILE_IMAGES_PATH${user.username}/"
            ).collectLatest { urlImagesList ->
                profileImage = urlImagesList.first()
            }
            updateMap["image"] = profileImage
        }
        updateMap["name"] = user.name
        updateMap["location"] = user.location
        try {
            firebaseFirestore.collection(USERS_COLLECTION_PATH).document(user.username)
                .update(updateMap)
                .await()
            val currentImages =
                getImagesInStoragePath("$IMAGES_STORAGE_PROFILE_IMAGES_PATH${user.username}/")
            currentImages.collectLatest { imageUrls ->
                imageUrls.forEach { imageUrl ->
                    if (imageUrl != profileImage) {
                        deleteImage(imageUrl).collectLatest {
                            if (it.data != null && it.data.contains("Error")) {
                                emit(Resource.Error("Error deleting image"))
                            }
                        }
                    }
                }
            }
            emit(Resource.Success("User database has been updated"))
        } catch (e: Exception) {
            emit(Resource.Error("Error: ${e.message.toString()}"))
        }
    }.catch {
        emit(Resource.Error("Error: ${it.message.toString()}"))
    }

    override fun deleteImage(imageUrl: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val imageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
            imageReference.delete().await()
            emit(Resource.Success("Image deleted successfully"))
        } catch (e: Exception) {
            emit(Resource.Error("Error deleting image: ${e.message.toString()}"))
        }
    }.catch {
        emit(Resource.Error("Error: ${it.message.toString()}"))
    }

    override fun getImagesInStoragePath(storagePath: String): Flow<List<String>> = flow {
        var imageUrls = listOf<String>()
        val storageReference = FirebaseStorage.getInstance().getReference(storagePath)
        val listResult = storageReference.listAll().await()
        imageUrls = listResult.items.map { it.downloadUrl.await().toString() }
        emit(imageUrls)
    }.catch {
        emit(listOf())
    }

    override fun getSpotsLocations(): Flow<List<SpotClusterItem>> = flow {
        try {
            val spotCollection =
                firebaseFirestore.collection(SPOTS_COLLECTION_PATH).get().await()
            val spotList = spotCollection.documents.mapNotNull { document ->
                val id = document.id
                val name = document.getString("name")
                val locationInLatLng = document.getGeoPoint("location_in_latlng")
                val spotReference =
                    firebaseFirestore.collection(SPOTS_COLLECTION_PATH).document(document.id)
                val featuredImages = document.get("featured_images") as List<String>
                val score: Float = document.get("score").toString().toFloat()
                val spotAttributesRefs =
                    document.get("attributes") as List<DocumentReference>
                var spotAttributes = listOf<SpotAttribute>()
                getSpotAttributesByDocRefs(spotAttributesRefs).collectLatest { attributes ->
                    spotAttributes = attributes
                }

                if (name != null && locationInLatLng != null) {
                    SpotClusterItem(
                        id = id,
                        spotReference = spotReference,
                        spot = Spot(
                            id = id,
                            featuredImages = featuredImages,
                            spottedBy = UserProfile(),
                            creationDate = "",
                            name = name,
                            description = "",
                            score = score,
                            visitedTimes = 0,
                            likes = 0,
                            locationInLatLng = locationInLatLng,
                            location = "",
                            attributes = spotAttributes,
                            spotReviews = listOf(),
                            spotPosts = listOf(),
                            likedBy = listOf()
                        ),
                        isSelected = false
                    )
                } else {
                    null
                }
            }
            emit(spotList)
        } catch (e: Exception) {
            Log.e("DatabaseRepository::getSpotsLocation", "Error: ${e.message}")
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
            val likedBySnapshot = documentReference.collection("liked_by").get().await()
            val likedByList = likedBySnapshot.documents.map { doc -> doc.id }
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
                spotPosts = spotPosts,
                likedBy = likedByList
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
            val isUserAdmin = userSnapshot.getBoolean("admin")
            val userProfile = UserProfile(
                username = username ?: "",
                email = email ?: "",
                provider = provider ?: "",
                creation_date = creationDate ?: "",
                name = usernameName ?: "",
                location = location ?: "",
                image = userImage ?: "",
                admin = isUserAdmin ?: false
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
                val type = attributeSnapshot.getString("type")
                if (description != null && title != null && icon != null && type != null) {
                    val attributeData = SpotAttribute(
                        id,
                        description,
                        title,
                        icon,
                        type
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
        var review = SpotReview()
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

    override fun getAllSpotAttributes(): Flow<List<SpotAttribute>> = flow {
        val spotAttributesList = mutableListOf<SpotAttribute>()

        val spotAttributesSnapshot =
            firebaseFirestore.collection(SPOT_ATTRIBUTES_COLLECTION).get().await()

        for (spotAttributeDoc in spotAttributesSnapshot.documents) {
            val id = spotAttributeDoc.id
            val title = spotAttributeDoc.getString("title")
            val description = spotAttributeDoc.getString("description")
            val icon = spotAttributeDoc.getString("icon")
            val type = spotAttributeDoc.getString("type")

            if (description != null && title != null && icon != null && type != null) {
                val spotAttribute = SpotAttribute(
                    id,
                    description,
                    title,
                    icon,
                    type
                )
                spotAttributesList.add(spotAttribute)
            }
        }
        emit(spotAttributesList)
    }.catch { exception ->
        Log.e("DatabaseRepository::getAllSpotAttributes", "Error: ${exception.message}")
        emit(mutableListOf())
    }

    override fun createSpotReview(
        spotReference: String,
        title: String,
        description: String,
        attributeList: List<SpotAttribute>,
        score: Int,
        author: UserProfile
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        val newReviewDocument = firebaseFirestore.collection(SPOTS_COLLECTION_PATH)
            .document(spotReference)
            .collection(SPOT_REVIEWS_COLLECTION)
            .document()

        val attributeRefs = attributeList.map {
            firebaseFirestore.collection(
                SPOT_ATTRIBUTES_COLLECTION
            ).document(it.id)
        }

        val userRef = firebaseFirestore.collection(USERS_COLLECTION_PATH).document(author.username)

        val newReviewData = hashMapOf(
            "description" to description,
            "title" to title,
            "posted_by" to userRef,
            "spot_attr" to attributeRefs,
            "creation_date" to Calendar.getInstance().time.toString(),
            "score" to score.toFloat()
        )

        newReviewDocument.set(newReviewData).await()
        emit(Resource.Success(newReviewDocument.id))
    }.catch { exception ->
        Log.e("DatabaseRepository::createSpotReview", "Error: ${exception.message}")
        emit(Resource.Success("Error: " + exception.message))
    }

    override fun createSpot(
        featuredImages: List<Uri>,
        spotTitle: String,
        spotDescription: String,
        spotLocation: LatLng,
        spotCountry: String?,
        spotLocality: String?,
        spotAuthor: String,
        spotAttributes: List<SpotAttribute>,
        spotScore: Int
    ): Flow<Resource<String>> = flow<Resource<String>> {
        emit(Resource.Loading())
        val newSpotDocument = firebaseFirestore.collection(SPOTS_COLLECTION_PATH)
            .document()
        val authorReference = firebaseFirestore.collection(USERS_COLLECTION_PATH)
            .document(spotAuthor)
        var featuredImagesList = listOf<String>()
        val attributesReferences = spotAttributes.map {
            firebaseFirestore.collection(
                SPOT_ATTRIBUTES_COLLECTION
            ).document(it.id)
        }
        var locationText = ""

        if (spotLocality.isNullOrBlank() && !spotCountry.isNullOrBlank()) {
            locationText = spotCountry.toString()
        } else if (!spotLocality.isNullOrBlank() && !spotCountry.isNullOrBlank()) {
            locationText = "$spotLocality, $spotCountry"
        }

        uploadImages(
            featuredImages,
            "$IMAGES_STORAGE_SPOTS_PATH${newSpotDocument.id}/"
        ).collectLatest { urlImagesList ->
            featuredImagesList = urlImagesList
        }

        val newSpot = hashMapOf(
            "featured_images" to featuredImagesList,
            "name" to spotTitle,
            "description" to spotDescription,
            "location_in_latlng" to GeoPoint(spotLocation.latitude, spotLocation.longitude),
            "location" to locationText,
            "spotted_by" to authorReference,
            "score" to spotScore,
            "likes" to 0,
            "posts" to emptyList<String>(),
            "creation_date" to Calendar.getInstance().time.toString(),
            "attributes" to attributesReferences
        )

        newSpotDocument.set(newSpot).await()
        emit(Resource.Success(newSpotDocument.id))
    }.catch { exception ->
        Log.e("DatabaseRepository::createSpot", "Error: ${exception.message}")
        emit(Resource.Success("Error: " + exception.message))
    }

    override fun updateSpot(
        spotReference: String,
        spotTitle: String,
        spotDescription: String,
        spotLocation: LatLng,
        spotCountry: String?,
        spotLocality: String?,
        spotAttributes: List<SpotAttribute>,
        spotScore: Int
    ): Flow<Resource<String>> = flow<Resource<String>> {
        emit(Resource.Loading())
        val spotDocumentReference = firebaseFirestore.document(spotReference)
        var locationText: String? = null

        if (spotLocality.isNullOrBlank() && !spotCountry.isNullOrBlank()) {
            locationText = spotCountry.toString()
        } else if (!spotLocality.isNullOrBlank() && !spotCountry.isNullOrBlank()) {
            locationText = "$spotLocality, $spotCountry"
        }

        val attributesReferences = spotAttributes.map {
            firebaseFirestore.collection(
                SPOT_ATTRIBUTES_COLLECTION
            ).document(it.id)
        }

        val updatedSpotData = hashMapOf(
            "name" to spotTitle,
            "description" to spotDescription,
            "location_in_latlng" to GeoPoint(spotLocation.latitude, spotLocation.longitude),
            "location" to locationText,
            "score" to spotScore,
            "attributes" to attributesReferences
        )

        spotDocumentReference.update(updatedSpotData).await()
        emit(Resource.Success(spotDocumentReference.id))
    }.catch { exception ->
        Log.e("DatabaseRepository::updateSpot", "Error: ${exception.message}")
        emit(Resource.Error("Error updating spot: ${exception.message}"))
    }

    override fun deleteSpot(spotReference: String): Flow<Resource<String>> =
        flow<Resource<String>> {
            emit(Resource.Loading())
            val spotDocumentReference = firebaseFirestore.document(spotReference)

            //Deleting associated posts and its subcollections/documents
            val spotPostsQuerySnapshot = firebaseFirestore.collection(POSTS_COLLECTION_PATH)
                .whereEqualTo("spot", spotDocumentReference)
                .get().await()
            val deletePostsTasks = mutableListOf<Task<Void>>()
            for (postDocumentSnapshot in spotPostsQuerySnapshot.documents) {
                val postDocumentReference = postDocumentSnapshot.reference

                // Delete comments subcollection
                val commentsCollection = postDocumentReference.collection("comments")
                deleteCollection(commentsCollection).collectLatest { }

                // Delete liked_by subcollection
                val likedByCollection = postDocumentReference.collection("liked_by")
                deleteCollection(likedByCollection).collectLatest { }

                val deleteTask = postDocumentSnapshot.reference.delete()
                deletePostsTasks.add(deleteTask)
            }

            Tasks.whenAllComplete(deletePostsTasks).await()

            //Delete spot images
            val featuredImagesList =
                spotDocumentReference.get().await().get("featured_images") as List<String>
            val deleteImagesTasks = mutableListOf<Task<Void>>()

            for (imageUrl in featuredImagesList) {
                val imageReference = firebaseStorage.getReferenceFromUrl(imageUrl)
                val deleteTask = imageReference.delete()
                deleteImagesTasks.add(deleteTask)
            }

            Tasks.whenAllComplete(deleteImagesTasks).await()

            //Delete posts images
            for (postDocumentSnapshot in spotPostsQuerySnapshot.documents) {
                val imagesList = postDocumentSnapshot.get("images") as List<String>?
                if (imagesList != null) {
                    val deletePostImagesTasks = mutableListOf<Task<Void>>()

                    for (imageUrl in imagesList) {
                        val imageReference = firebaseStorage.getReferenceFromUrl(imageUrl)
                        val deleteTask = imageReference.delete()
                        deletePostImagesTasks.add(deleteTask)
                    }

                    Tasks.whenAllComplete(deletePostImagesTasks).await()
                }
            }

            // Delete liked_by subcollection
            val likedByCollection = spotDocumentReference.collection("liked_by")
            deleteCollection(likedByCollection).collectLatest { }

            // Delete spot_reviews subcollection
            val spotReviewsCollection = spotDocumentReference.collection("spot_reviews")
            deleteCollection(spotReviewsCollection).collectLatest { }

            //Delete spot
            spotDocumentReference.delete().await()
            emit(Resource.Success("deleted successfully"))
        }.catch { exception ->
            Log.e("DatabaseRepository::deleteSpot", "Error: ${exception.message}")
            emit(Resource.Error("Error deleting spot: ${exception.message}"))
        }

    override fun deleteCollection(collectionRef: CollectionReference): Flow<Unit> = flow {
        val batchSize = 1000
        var query = collectionRef.limit(batchSize.toLong())

        while (true) {
            val snapshot = query.get().await()

            if (snapshot.isEmpty) {
                break
            }

            val batch = collectionRef.firestore.batch()
            for (documentSnapshot in snapshot) {
                batch.delete(documentSnapshot.reference)
            }

            batch.commit().await()

            if (snapshot.size() < batchSize) {
                break
            }

            val lastDocument = snapshot.documents[snapshot.size() - 1]
            query = collectionRef.startAfter(lastDocument)
        }
        emit(Unit)
    }

    override fun sendReport(
        reportType: String,
        reporterUsername: String,
        reportIssue: String,
        reportDescription: String,
        documentReference: String
    ): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        val newReportDocument = firebaseFirestore.collection(REPORTS_COLLECTION_PATH)
            .document()
        val newReport = hashMapOf(
            "type" to reportType,
            "reporter" to reporterUsername,
            "issue" to reportIssue,
            "description" to reportDescription,
            "doc_reference" to firebaseFirestore.document(documentReference),
            "date" to Calendar.getInstance().time.toString()
        )
        newReportDocument.set(newReport).await()
        emit(Resource.Success(newReportDocument.id))
    }.catch { exception ->
        Log.e("DatabaseRepository::sendReport", "Error: ${exception.message}")
        emit(Resource.Success("Error: " + exception.message))
    }

    override fun getSpotReportsOptions(): Flow<List<String>> = flow {
        val reportOptionsReference =
            firebaseFirestore.collection(REPORT_OPTIONS_SPOT_COLLECTION_PATH).get().await()
        val reportOptionsList = mutableListOf<String>()
        reportOptionsReference.documents.mapNotNull { document ->
            val reportOption = document.getString("title")
            reportOptionsList.add(reportOption.toString())
        }
        emit(reportOptionsList)
    }.catch { exception ->
        Log.e("DatabaseRepository::getSpotReportsOptions", "Error: ${exception.message}")
        emit(mutableListOf())
    }

    override fun getReportsList(): Flow<List<Report>> = flow {
        val reportsList = mutableListOf<Report>()

        val reportsListSnapshot =
            firebaseFirestore.collection(REPORTS_COLLECTION_PATH).get().await()
        for (reportDoc in reportsListSnapshot.documents) {
            val reportId = reportDoc.id
            val type = reportDoc.getString("type")
            val reporter = reportDoc.get("reporter")
            val issue = reportDoc.getString("issue")
            val docReference: DocumentReference? = reportDoc.getDocumentReference("doc_reference")
            val description = reportDoc.getString("description")
            val date = reportDoc.getString("date")
            val assignedBy = reportDoc.getString("assigned_by") ?: ""

            if (reportId != "reportOptions" && type != null && reporter != null && issue != null && docReference != null && description != null) {
                val report = Report(
                    reportId,
                    date.toString(),
                    description,
                    docReference,
                    issue,
                    reporter.toString(),
                    type,
                    assignedBy
                )
                reportsList.add(report)
            }
        }
        emit(reportsList)
    }.catch { exception ->
        Log.e("DatabaseRepository::getReportsList", "Error: ${exception.message}")
        emit(mutableListOf())
    }

    override fun assignReport(username: String, reportId: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        val reportDocumentReference =
            firebaseFirestore.collection(REPORTS_COLLECTION_PATH).document(reportId)
        val updatedReport = hashMapOf<String, Any>(
            "assigned_by" to username
        )
        reportDocumentReference.update(updatedReport).await()
        emit(Resource.Success("Updated successfully"))
    }.catch { exception ->
        Log.e("DatabaseRepository::assignReport", "Error: ${exception.message}")
        emit(Resource.Error("Error updating report: ${exception.message}"))
    }

    override fun deleteReport(reportId: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        val reportDocumentReference =
            firebaseFirestore.collection(REPORTS_COLLECTION_PATH).document(reportId)
        reportDocumentReference.delete().await()
        emit(Resource.Success("Deleted successfully"))
    }.catch { exception ->
        Log.e("DatabaseRepository::deleteReport", "Error: ${exception.message}")
        emit(Resource.Error("Error deleting report: ${exception.message}"))
    }

    override fun getSpotPostsByUsername(username: String): Flow<List<SpotPost>> = flow {
        val postsList = mutableListOf<SpotPost>()
        val userReference = firebaseFirestore.collection(USERS_COLLECTION_PATH).document(username)
        val querySnapshot = firebaseFirestore.collection(POSTS_COLLECTION_PATH)
            .whereEqualTo("author", userReference)
            .get()
            .await()

        for (documentSnapshot in querySnapshot.documents) {
            val images = documentSnapshot.get("images") as List<String>
            val id = documentSnapshot.id
            val creationDate = documentSnapshot.getString("creation_date")
            val post = SpotPost(
                id = id,
                description = "",
                spotRef = "",
                images = images,
                author = UserProfile(),
                creation_date = creationDate ?: "",
                comments = listOf(),
                likedBy = listOf(),
                likes = 0
            )
            postsList.add(post)
        }
        emit(postsList)
    }.catch { exception ->
        Log.e("DatabaseRepository::getSpotPostsByUsername", "Error: ${exception.message}")
        emit(mutableListOf())
    }

    override fun getSpotsByUsername(username: String): Flow<List<Spot>> = flow {
        val spotsList = mutableListOf<Spot>()
        val userReference = firebaseFirestore.collection(USERS_COLLECTION_PATH).document(username)
        val querySnapshot = firebaseFirestore.collection(SPOTS_COLLECTION_PATH)
            .whereEqualTo("spotted_by", userReference)
            .get()
            .await()

        for (documentSnapshot in querySnapshot.documents) {
            val images = documentSnapshot.get("featured_images") as List<String>
            val date = documentSnapshot.getString("creation_date")
            val location = documentSnapshot.getString("location")
            val name = documentSnapshot.getString("name")
            val id = documentSnapshot.id
            val spot = Spot(
                id = id,
                featuredImages = images,
                spottedBy = UserProfile(),
                creationDate = date ?: "",
                name = name ?: "",
                description = "",
                score = 0.0f,
                visitedTimes = 0,
                likes = 0,
                locationInLatLng = GeoPoint(0.0, 0.0),
                location = location ?: "",
                attributes = listOf(),
                spotReviews = listOf(),
                spotPosts = listOf(),
                likedBy = listOf()
            )
            spotsList.add(spot)
        }

        emit(spotsList)
    }.catch { exception ->
        Log.e("DatabaseRepository::getSpotsByUsername", "Error: ${exception.message}")
        emit(mutableListOf())
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
    fun getSpotPostsByUsername(username: String): Flow<List<SpotPost>>
    fun getSpotsByUsername(username: String): Flow<List<Spot>>
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
    fun modifyUserSpotLike(spotReference: String, username: String): Flow<Resource<String>>
    fun checkIfPostIsLikedByUser(postReference: String, username: String): Flow<Resource<Boolean>>
    fun getSpotReviewByDocRef(spotReference: String, docReference: String): Flow<SpotReview>
    fun getAllSpotAttributes(): Flow<List<SpotAttribute>>
    fun createSpotReview(
        spotReference: String,
        title: String,
        description: String,
        attributeList: List<SpotAttribute>,
        score: Int,
        author: UserProfile
    ): Flow<Resource<String>>

    fun createSpot(
        featuredImages: List<Uri>,
        spotTitle: String,
        spotDescription: String,
        spotLocation: LatLng,
        spotCountry: String?,
        spotLocality: String?,
        spotAuthor: String,
        spotAttributes: List<SpotAttribute>,
        spotScore: Int
    ): Flow<Resource<String>>

    fun updateSpot(
        spotReference: String,
        spotTitle: String,
        spotDescription: String,
        spotLocation: LatLng,
        spotCountry: String?,
        spotLocality: String?,
        spotAttributes: List<SpotAttribute>,
        spotScore: Int
    ): Flow<Resource<String>>

    fun deleteSpot(
        spotReference: String
    ): Flow<Resource<String>>

    fun deleteCollection(
        collectionRef: CollectionReference
    ): Flow<Unit>

    fun sendReport(
        reportType: String,
        reporterUsername: String,
        reportIssue: String,
        reportDescription: String,
        documentReference: String
    ): Flow<Resource<String>>

    fun getSpotReportsOptions(): Flow<List<String>>
    fun getReportsList(): Flow<List<Report>>
    fun assignReport(
        username: String,
        reportId: String
    ): Flow<Resource<String>>

    fun deleteReport(
        reportId: String
    ): Flow<Resource<String>>

    fun getImagesInStoragePath(
        storagePath: String
    ): Flow<List<String>>

    fun deleteImage(
        imageUrl: String
    ): Flow<Resource<String>>

}
