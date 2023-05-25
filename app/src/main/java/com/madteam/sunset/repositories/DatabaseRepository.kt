package com.madteam.sunset.repositories

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.madteam.sunset.model.Spot
import com.madteam.sunset.model.SpotAttribute
import com.madteam.sunset.model.SpotClusterItem
import com.madteam.sunset.model.SpotReview
import com.madteam.sunset.model.UserProfile
import com.madteam.sunset.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

private const val USERS_COLLECTION_PATH = "users"
private const val SPOTS_LOCATIONS_COLLECTION_PATH = "spots_locations"
private const val SPOTS_COLLECTION_PATH = "spots"

class DatabaseRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
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
    }.catch {
        emit(Resource.Error(it.message.toString()))
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
            var spottedBy = UserProfile()
            if (spottedByDocRef != null) {
                getUserProfileByDocRef(spottedByDocRef.path).collectLatest { profile ->
                    spottedBy = profile
                }
            }


            //Spot attributes data
            val attributesRefs = documentSnapshot.get("attributes") as List<DocumentReference>
            val attributesList = mutableListOf<SpotAttribute>()
            for (attributeRef in attributesRefs) {
                val attributeSnapshot = attributeRef.get().await()
                val attributeId = attributeSnapshot.id
                val attributeTitle = attributeSnapshot.getString("title")
                val attributeDescription = attributeSnapshot.getString("description")
                val attributeIcon = attributeSnapshot.getString("icon")
                val attributeFavorable = attributeSnapshot.getBoolean("favorable")
                if (attributeDescription != null && attributeTitle != null && attributeIcon != null && attributeFavorable != null) {
                    val attributeData = SpotAttribute(
                        attributeId,
                        attributeDescription,
                        attributeTitle,
                        attributeIcon,
                        attributeFavorable
                    )
                    attributesList.add(attributeData)
                }

            }

            //Spot reviews data
            val spotReviewsRef = documentReference.collection("spot_reviews")
            val spotReviewsList = mutableListOf<SpotReview>()
            spotReviewsRef.get().await().forEach { spotReviewsSnapshow ->
                val id= spotReviewsSnapshow.id
                val description = spotReviewsSnapshow.getString("description")
                val title = spotReviewsSnapshow.getString("title")
                val postedBy = spotReviewsSnapshow.getDocumentReference("posted_by")
                val spotAttributes = spotReviewsSnapshow.get("spot_attr") as List<SpotAttribute>
                val creationDate = spotReviewsSnapshow.getString("creation_date")
                val score = spotReviewsSnapshow.getDouble("score")
                if (description != null && title != null && postedBy != null && creationDate != null && score != null) {
                    spotReviewsList.add(
                        SpotReview(
                            id,
                            description,
                            title,
                            UserProfile(),
                            spotAttributes,
                            creationDate,
                            score.toFloat()
                        )
                    )
                }
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
                attributes = attributesList,
                spotReviews = listOf(),
                spotPosts = listOf()
            )
            emit(spotData)
        } else {
            emit(Spot())
        }
    }.catch { exception ->
        Log.e("DatabaseRepository::getSpotByDocRef", "Error: ${exception.message}")
        emit(Spot())
    }

    override fun getUserProfileByDocRef(docRef: String): Flow<UserProfile> = flow {
        val documentReference = firebaseFirestore.document(docRef)
        val userSnapshot = documentReference.get().await()

        if (userSnapshot.exists()) {
            val username = userSnapshot.getString("username")
            val userImage = userSnapshot.getString("image")
            val usernameName = userSnapshot.getString("name")
            val userProfile = UserProfile(
                username = username ?: "",
                "",
                "",
                "",
                name = usernameName ?: "",
                "",
                image = userImage  ?: ""
            )
            emit(userProfile)
        }
    }.catch { exception ->
        Log.e("DatabaseRepository::getUserProfileByDocRef", "Error: ${exception.message}")
        emit(UserProfile())
    }


}






interface DatabaseContract {

    fun createUser(email: String, username: String, provider: String): Flow<Resource<String>>
    fun getUserByEmail(email: String, userProfileCallback: (UserProfile) -> Unit)
    fun updateUser(user: UserProfile): Flow<Resource<String>>
    fun getSpotsLocations(): Flow<List<SpotClusterItem>>
    fun getSpotByDocRef(docRef: String): Flow<Spot>
    fun getUserProfileByDocRef(docRef: String): Flow<UserProfile>
}
