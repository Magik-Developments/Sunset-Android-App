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
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        val userDocument = firebaseFirestore.collection(USERS_COLLECTION_PATH).document(username)
        val documentSnapshot = userDocument.get().await()
        if (documentSnapshot.exists()) {
            emit(Resource.Error("El usuario ya existe en la base de datos"))
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
        emit(Resource.Success(Unit))
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
}

interface DatabaseContract {

    fun createUser(email: String, username: String, provider: String): Flow<Resource<Unit>>
    fun getUserByEmail(email: String, userProfileCallback: (UserProfile) -> Unit)
}
