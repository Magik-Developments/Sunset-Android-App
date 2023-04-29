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

    override fun doSignUp(email: String, password: String): Flow<Resource<AuthResult>> =
        flow {
            emit(Resource.Loading())
            firebaseAuth.createUserWithEmailAndPassword(email, password).await().let { result ->
                emit(Resource.Success(result))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }

    override suspend fun doSignInWithPasswordAndEmail(
        email: String,
        password: String
    ): Result<AuthResult> = kotlin.runCatching { // TODO: Yoy soy muy fan de la clase Result de kotlin.
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
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

    override fun getCurrentUser(): Flow<Resource<FirebaseUser>> =
        flow {
            emit(Resource.Loading())  // No lo necesitas ya que firebaseAuth.currentUser no está suspend
            firebaseAuth.currentUser?.let { currentUser ->
                emit(Resource.Success(currentUser))
            }
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
}

// Esto es un contrato genérico de Autenticación, no hace falta asociarlo a Firebase.
// Si el dia de mñn quiere otro sistema de Auth, este contrato sigue siendo valido.
interface AuthContract {
    fun doSignUp(email: String, password: String): Flow<Resource<AuthResult>>
    suspend fun doSignInWithPasswordAndEmail(email: String, password: String): Result<AuthResult>
    fun deleteCurrentUser(): Flow<Resource<Unit>>
    fun getCurrentUser(): Flow<Resource<FirebaseUser>>
}
