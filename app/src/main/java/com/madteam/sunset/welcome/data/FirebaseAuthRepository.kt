package com.madteam.sunset.welcome.data

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface FireBaseAuthRepositoryContract {

  suspend fun doSignUp(email: String, password: String, username: String): AuthResult?
  suspend fun checkIfEmailExists(email: String): Boolean
}

class FirebaseAuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) :
  FireBaseAuthRepositoryContract {

  override suspend fun doSignUp(email: String, password: String, username: String): AuthResult? =
    firebaseAuth.createUserWithEmailAndPassword(email, password).await()

  override suspend fun checkIfEmailExists(email: String): Boolean {
    val signInMethods = firebaseAuth.fetchSignInMethodsForEmail(email).await().signInMethods
    return !signInMethods.isNullOrEmpty()
  }
}