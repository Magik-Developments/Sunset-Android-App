package com.madteam.sunset.welcome.data

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.madteam.sunset.common.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface FireBaseAuthRepositoryContract {

  fun doSignUp(email: String, password: String): Flow<Resource<AuthResult>>
  fun doSignInWithPasswordAndEmail(email: String, password: String): Flow<Resource<AuthResult>>
  fun deleteCurrentUser(): Flow<Resource<Unit>>
  fun getCurrentUser(): Flow<Resource<FirebaseUser>>
}

class FirebaseAuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) :
  FireBaseAuthRepositoryContract {

  override fun doSignUp(email: String, password: String): Flow<Resource<AuthResult>> =
    flow {
      emit(Resource.Loading())
      val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
      emit(Resource.Success(result))
    }.catch {
      emit(Resource.Error(it.message.toString()))
    }

  override fun doSignInWithPasswordAndEmail(
    email: String,
    password: String
  ): Flow<Resource<AuthResult>> =
    flow {
      emit(Resource.Loading())
      val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
      emit(Resource.Success(result))
    }.catch {
      emit(Resource.Error(it.message.toString()))
    }

  override fun deleteCurrentUser(): Flow<Resource<Unit>> =
    flow {
      emit(Resource.Loading())
      firebaseAuth.currentUser?.delete()
      emit(Resource.Success(Unit))
    }.catch {
      emit(Resource.Error(it.message.toString()))
    }

  override fun getCurrentUser(): Flow<Resource<FirebaseUser>> =
    flow {
      emit(Resource.Loading())
      val currentUser = firebaseAuth.currentUser
      emit(Resource.Success(currentUser!!))
    }.catch {
      emit(Resource.Error(it.message.toString()))
    }

}