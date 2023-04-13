package com.madteam.sunset.welcome.data

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface FireBaseAuthRepositoryContract {

    suspend fun doSignUp(email: String, password: String, username: String): AuthResult?
}

class FirebaseAuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    FireBaseAuthRepositoryContract {

    override suspend fun doSignUp(email: String, password: String, username: String): AuthResult? =
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
}