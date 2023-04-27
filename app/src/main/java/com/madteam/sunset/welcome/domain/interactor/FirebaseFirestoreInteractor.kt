package com.madteam.sunset.welcome.domain.interactor

import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import com.madteam.sunset.common.utils.Resource
import com.madteam.sunset.welcome.data.FirebaseFirestoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface FirebaseFirestoreInteractorContract {
  fun createUserDatabase(email: String, username: String, provider: String): Flow<Resource<Unit>>
  fun getProfileUsername(email: String): Flow<Resource<String>>
}

class FirebaseFirestoreInteractor @Inject constructor(
  private val firebaseFirestoreRepository: FirebaseFirestoreRepository
) : FirebaseFirestoreInteractorContract {

  @RequiresApi(VERSION_CODES.O)
  override fun createUserDatabase(email: String, username: String, provider: String): Flow<Resource<Unit>> =
    firebaseFirestoreRepository.createUserDatabase(email, username, provider)

  override fun getProfileUsername(email: String): Flow<Resource<String>> =
    firebaseFirestoreRepository.getProfileUsername(email)

}