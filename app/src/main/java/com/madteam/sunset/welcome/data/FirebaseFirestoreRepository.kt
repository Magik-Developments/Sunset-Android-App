package com.madteam.sunset.welcome.data

import com.google.firebase.firestore.FirebaseFirestore
import com.madteam.sunset.common.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

interface FirebaseFirestoreRepositoryContract {
  fun createUserDatabase(email: String, username: String, provider: String): Flow<Resource<Unit>>
}

class FirebaseFirestoreRepository @Inject constructor(private val firebaseFirestore: FirebaseFirestore) :
  FirebaseFirestoreRepositoryContract {

  override fun createUserDatabase(
    email: String,
    username: String,
    provider: String
  ): Flow<Resource<Unit>> = flow {
    emit(Resource.Loading())
    val user = hashMapOf(
      "username" to username,
      "email" to email,
      "provider" to provider,
      "creation_date" to Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid"))
    )
    firebaseFirestore.collection("users").add(user).await()
    emit(Resource.Success(Unit))
  }.catch {
    emit(Resource.Error(it.message.toString()))
  }

}
