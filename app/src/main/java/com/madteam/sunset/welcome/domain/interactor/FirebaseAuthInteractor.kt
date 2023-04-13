package com.madteam.sunset.welcome.domain.interactor

import com.google.firebase.auth.AuthResult
import com.madteam.sunset.welcome.data.FirebaseAuthRepository
import javax.inject.Inject

interface FirebaseAuthInteractorContract {

  suspend fun doSignUp(email: String, password: String, username: String): AuthResult?

  suspend fun checkIfEmailExists(email: String): Boolean
}

class FirebaseAuthInteractor @Inject constructor(
  private val firebaseAuthRepository: FirebaseAuthRepository
) :
  FirebaseAuthInteractorContract {

  override suspend fun doSignUp(email: String, password: String, username: String): AuthResult? =
    firebaseAuthRepository.doSignUp(email, password, username)

  override suspend fun checkIfEmailExists(email: String): Boolean {
    return firebaseAuthRepository.checkIfEmailExists(email)
  }
}