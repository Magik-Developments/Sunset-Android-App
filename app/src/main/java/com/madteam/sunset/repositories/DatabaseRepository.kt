package com.madteam.sunset.repositories

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.madteam.sunset.model.Spot
import com.madteam.sunset.model.SpotAttributes
import com.madteam.sunset.model.SpotClusterItem
import com.madteam.sunset.model.UserProfile
import com.madteam.sunset.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
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
        try {
            val documentReference = firebaseFirestore.document(docRef)
            val documentSnapshot = documentReference.get().await()

            if (documentSnapshot.exists()) {
                val id = documentSnapshot.id
                val creationDate = documentSnapshot.getString("creation_date")
                val name = documentSnapshot.getString("name")
                val description = documentSnapshot.getString("description")
                val score = documentSnapshot.getDouble("score")
                val visitedTimes = documentSnapshot.get("visited_times")
                val likes = documentSnapshot.get("likes")
                val locationInLatLng = documentSnapshot.getGeoPoint("location_in_latlng")
                val location = documentSnapshot.getString("location")

                //User spotted by data
                val userRef = documentSnapshot.getDocumentReference("spotted_by")
                val userSnapshot = userRef!!.get().await()
                val userId = userSnapshot.id
                val username = userSnapshot.getString("username")
                val usernameName = userSnapshot.getString("name")
                val spottedBy = UserProfile(
                    username = username ?: "",
                    "",
                    "",
                    "",
                    name = name ?: "",
                    "",
                )

                //Spot attributes data
                val attributesRefs = documentSnapshot.get("attributes") as List<DocumentReference>
                val attributesList = mutableListOf<SpotAttributes>()
                for (attributeRef in attributesRefs) {
                    val attributeSnapshot = attributeRef.get().await()
                    val attributeId = attributeSnapshot.id
                    val attributeTitle = attributeSnapshot.getString("title")
                    val attributeDescription = attributeSnapshot.getString("description")
                    val attributeIcon = attributeSnapshot.getString("icon")
                    val attributeFavorable = attributeSnapshot.getBoolean("favorable")
                    if (attributeDescription != null && attributeTitle != null && attributeIcon != null && attributeFavorable != null) {
                        val attributeData = SpotAttributes(attributeId, attributeDescription, attributeTitle, attributeIcon, attributeFavorable)
                        attributesList.add(attributeData)
                    }

                }

                val spotData = Spot(
                    id = id,
                    spottedBy = spottedBy,
                    creationDate = creationDate ?: "",
                    name = name ?: "",
                    description = description ?: "",
                    score = score?.toFloat() ?: 0.0f,
                    visitedTimes = visitedTimes.toString().toInt(),
                    likes = likes.toString().toInt(),
                    locationInLatLng = locationInLatLng ?: GeoPoint(0.0, 0.0),
                    location = location ?: "",
                    attributes = attributesList
                )

                emit(spotData)
            } else {
                emit(Spot())
            }
        } catch (e: Exception) {
            emit(Spot())
        }
    }
}


interface DatabaseContract {

    fun createUser(email: String, username: String, provider: String): Flow<Resource<String>>
    fun getUserByEmail(email: String, userProfileCallback: (UserProfile) -> Unit)
    fun updateUser(user: UserProfile): Flow<Resource<String>>
    fun getSpotsLocations(): Flow<List<SpotClusterItem>>
    fun getSpotByDocRef(docRef: String): Flow<Spot>
}
