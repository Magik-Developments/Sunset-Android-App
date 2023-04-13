package com.dgalan.rmultimateapp.login.domain.interactor

import com.dgalan.rmultimateapp.login.data.repository.FirebaseAuthRepository
import javax.inject.Inject

interface FirebaseAuthInteractorContract {
    suspend fun doLogin(email: String, password: String)
}

class FirebaseAuthInteractor @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository
) :
    FirebaseAuthInteractorContract {

    override suspend fun doLogin(email: String, password: String) {
        firebaseAuthRepository.doLogin(email, password)
    }
}