package com.madteam.sunset.data.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.madteam.sunset.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
  private val firebaseAuth: FirebaseAuth
) : AuthContract {

  override fun doSignUpWithPasswordAndEmail(
    email: String,
    password: String
  ): Flow<Resource<AuthResult?>> =
    flow {
      emit(Resource.Loading())
      logout()
      firebaseAuth.createUserWithEmailAndPassword(email, password).await().let { result ->
        emit(Resource.Success(result))
      }
    }.catch {
      emit(Resource.Error(it.message.toString()))
    }

  override fun doSignInWithPasswordAndEmail(
    email: String,
    password: String
  ): Flow<Resource<AuthResult?>> =
    flow {
      emit(Resource.Loading())
      firebaseAuth.signInWithEmailAndPassword(email, password).await().let { result ->
        emit(Resource.Success(result))
      }
    }.catch {
      emit(Resource.Error(it.message.toString()))
    }

  override fun deleteCurrentUser(): Flow<Resource<Unit>> =
    flow {
      emit(Resource.Loading())
      firebaseAuth.currentUser?.delete()?.await()?.let {
        emit(Resource.Success(Unit))
      }
    }.catch {
      emit(Resource.Error(it.message.toString()))
    }

  override fun getCurrentUser(): FirebaseUser? =
    firebaseAuth.currentUser

  override fun logout() = firebaseAuth.signOut()

  override fun resetPasswordWithEmailIntent(email: String) {
    firebaseAuth.sendPasswordResetEmail(email)
  }

  override fun sendVerifyEmailIntent() {
    getCurrentUser()?.sendEmailVerification()
  }

  override fun checkIfUserEmailIsVerified(credential: String): Flow<Resource<Boolean>> =
    flow {
      emit(Resource.Loading())
      val isVerified = firebaseAuth.currentUser?.isEmailVerified
      if (isVerified == true) {
        emit(Resource.Success(true))
      } else if (isVerified == false) {
        emit(Resource.Success(false))
      }
    }.catch {
      emit(Resource.Error(it.message.toString()))
    }

  override fun reauthenticateUser(credential: String): Flow<Resource<Unit>> =
    flow {
      val userEmail = firebaseAuth.currentUser?.email!!
      val emailCredential = EmailAuthProvider
        .getCredential(userEmail, credential)
      firebaseAuth.currentUser?.reauthenticate(emailCredential)?.await()
    }
}

interface AuthContract {

  fun doSignUpWithPasswordAndEmail(email: String, password: String): Flow<Resource<AuthResult?>>
  fun doSignInWithPasswordAndEmail(email: String, password: String): Flow<Resource<AuthResult?>>
  fun deleteCurrentUser(): Flow<Resource<Unit>>
  fun getCurrentUser(): FirebaseUser?
  fun logout()
  fun resetPasswordWithEmailIntent(email: String)
  fun sendVerifyEmailIntent()
  fun checkIfUserEmailIsVerified(credential: String): Flow<Resource<Boolean>>
  fun reauthenticateUser(credential: String): Flow<Resource<Unit>>
}
