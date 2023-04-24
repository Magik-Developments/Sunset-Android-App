package com.madteam.sunset.welcome.domain.interactor

import com.madteam.sunset.common.utils.Resource
import com.madteam.sunset.welcome.data.FirebaseFirestoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface FirebaseFirestoreInteractorContract {
  fun createUserDatabase(email: String, username: String, provider: String): Flow<Resource<Unit>>
}

class FirebaseFirestoreInteractor @Inject constructor(
  private val firebaseFirestoreRepository: FirebaseFirestoreRepository
) : FirebaseFirestoreInteractorContract {

  override fun createUserDatabase(email: String, username: String, provider: String): Flow<Resource<Unit>> =
    firebaseFirestoreRepository.createUserDatabase(email, username, provider)

}