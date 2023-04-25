package com.madteam.sunset.welcome.data

import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.madteam.sunset.common.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

interface FirebaseFirestoreRepositoryContract {
  fun createUserDatabase(email: String, username: String, provider: String): Flow<Resource<Unit>>
}

class FirebaseFirestoreRepository @Inject constructor(private val firebaseFirestore: FirebaseFirestore) :
  FirebaseFirestoreRepositoryContract {

  @RequiresApi(VERSION_CODES.O)
  override fun createUserDatabase(
    email: String,
    username: String,
    provider: String
  ): Flow<Resource<Unit>> = flow {
    emit(Resource.Loading())

    val userDocument = firebaseFirestore.collection("users").document(username)
    val documentSnapshot = userDocument.get().await()
    if (documentSnapshot.exists()) {
      emit(Resource.Error("El usuario ya existe en la base de datos"))
      return@flow
    }

    val currentDate = LocalDateTime.now().toString()
    val user = hashMapOf(
      "username" to username,
      "email" to email,
      "provider" to provider,
      "creation_date" to currentDate
    )

    firebaseFirestore.collection("users").document(username).set(user).await()
    emit(Resource.Success(Unit))
  }.catch {
    emit(Resource.Error(it.message.toString()))
  }
}
