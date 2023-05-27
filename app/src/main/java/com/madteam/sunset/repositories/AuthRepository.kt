package com.madteam.sunset.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.madteam.sunset.utils.Resource
import com.madteam.sunset.utils.Result
import com.madteam.sunset.utils.runCatchingException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//fun <R> showProgressDialogAndHandleException(block: suspend () -> R): Flow<Result<R>> = flow {
//    emit(Result.Loading())
//    try {
//        emit(Result.Success(data = block()))
//    } catch (ex: Exception) {
//        emit(Result.Error(ex))
//    }
//}


class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthContract {

    override suspend fun doSignUpWithPasswordAndEmail(email: String, password: String): Result<AuthResult?> =
        runCatchingException {
            logout()
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        }

//    override fun doSignUpWithPasswordAndEmail(
//        email: String,
//        password: String
//    ): Flow<Resource<AuthResult?>> =
//        flow {
//            emit(Resource.Loading())
//            logout()
//            firebaseAuth.createUserWithEmailAndPassword(email, password).await().let { result ->
//                emit(Resource.Success(result))
//            }
//        }.catch {
//            emit(Resource.Error(it.message.toString()))
//        }


    override suspend fun doSignInWithPasswordAndEmail(email: String, password: String): Result<AuthResult?> =
        runCatchingException {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        }

//    override suspend fun doSignInWithPasswordAndEmail(email: String, password: String): Result<AuthResult?> {
//        return try {
//            firebaseAuth.signInWithEmailAndPassword(email, password).await().let { authResult ->
//                Result.Success(data = authResult)
//            }
//        } catch (ex: Exception) {
//            Result.Error(ex)
//        }
//    }


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

    override fun reAuthenticateUser(credential: String): Flow<Resource<Unit>> =
        flow {
            val userEmail = firebaseAuth.currentUser?.email!!
            val emailCredential = EmailAuthProvider
                .getCredential(userEmail, credential)
            firebaseAuth.currentUser?.reauthenticate(emailCredential)?.await()
        }
}

interface AuthContract {

    suspend fun doSignUpWithPasswordAndEmail(email: String, password: String): Result<AuthResult?>
    suspend fun doSignInWithPasswordAndEmail(email: String, password: String): Result<AuthResult?>

    fun deleteCurrentUser(): Flow<Resource<Unit>>
    fun getCurrentUser(): FirebaseUser?
    fun logout()
    fun resetPasswordWithEmailIntent(email: String)
    fun sendVerifyEmailIntent()
    fun checkIfUserEmailIsVerified(credential: String): Flow<Resource<Boolean>>
    fun reAuthenticateUser(credential: String): Flow<Resource<Unit>>
}
