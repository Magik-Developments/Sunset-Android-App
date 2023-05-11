package com.madteam.sunset.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.madteam.sunset.model.UserProfile
import com.madteam.sunset.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

private const val USERS_COLLECTION_PATH = "users"

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
}

interface DatabaseContract {

  fun createUser(email: String, username: String, provider: String): Flow<Resource<String>>
  fun getUserByEmail(email: String, userProfileCallback: (UserProfile) -> Unit)
  fun updateUser(user: UserProfile): Flow<Resource<String>>
}
