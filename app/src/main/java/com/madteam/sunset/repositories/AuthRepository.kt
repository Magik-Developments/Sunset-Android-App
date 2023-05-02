package com.madteam.sunset.repositories

import com.google.firebase.auth.AuthResult
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
}

interface AuthContract {

    fun doSignUpWithPasswordAndEmail(email: String, password: String): Flow<Resource<AuthResult?>>
    fun doSignInWithPasswordAndEmail(email: String, password: String): Flow<Resource<AuthResult?>>
    fun deleteCurrentUser(): Flow<Resource<Unit>>
    fun getCurrentUser(): FirebaseUser?
    fun logout()
    fun resetPasswordWithEmailIntent(email: String)
}
