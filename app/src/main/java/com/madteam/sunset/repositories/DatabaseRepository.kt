package com.madteam.sunset.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.madteam.sunset.model.Spot
import com.madteam.sunset.model.UserProfile
import com.madteam.sunset.utils.Resource
import com.madteam.sunset.model.SpotClusterItem
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
    val userDocument = firebaseFirestore.collection(USERS_COLLECTION_PATH).document(user.username)
    val documentSnapshot = userDocument.get().await()
    if (!documentSnapshot.exists()) {
      emit(Resource.Error("e_user_database_not_found"))
      return@flow
    }
    val updateMap = HashMap<String, Any?>()
    updateMap["name"] = user.name
    updateMap["location"] = user.location
    firebaseFirestore.collection(USERS_COLLECTION_PATH).document(user.username).update(updateMap)
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
          SpotClusterItem(id = id, name = name, spot = spot.toString(), location = location, isSelected = false)
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

      }
    } catch (e: Exception) {

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
