package com.madteam.sunset.domain.usecases

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.madteam.sunset.data.model.UserProfile
import com.madteam.sunset.data.model.toEntity
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.data.repositories.DatabaseRepository
import javax.inject.Inject

class GetMyUserProfileInfoUseCase @Inject constructor(
    private val repository: DatabaseRepository,
    private val authRepository: AuthRepository,
    private val databaseRepository: DatabaseRepository,
    private val firestore: FirebaseFirestore
) {

    private var username: String = ""

    suspend operator fun invoke(): UserProfile {
        try {
            return repository.getMyUserProfileInfoFromDatabase()
        } catch (e: Exception) {
            Log.d("GetUserProfileInfoUseCase", "Error: ${e.message}")
        }
        try {
            authRepository.getCurrentUser()?.let { user ->
                username = databaseRepository.getUserByEmail(user.email!!).username
            }
            val usernameDocRef = firestore.collection("users").document(username)
            val myUserInfo = repository.getUserProfileByDocRef(usernameDocRef)
            repository.insertMyUserProfileInfoOnDatabase(myUserInfo.toEntity())
            return myUserInfo
        } catch (e: Exception) {
            Log.d("GetUserProfileInfoUseCase", "Error: ${e.message}")
        }
        return UserProfile()
    }
}