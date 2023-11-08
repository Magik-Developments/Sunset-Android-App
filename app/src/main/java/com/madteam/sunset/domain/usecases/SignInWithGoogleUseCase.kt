package com.madteam.sunset.domain.usecases

import com.google.firebase.auth.AuthResult
import com.madteam.sunset.data.model.UserProfile
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.utils.Resource
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val databaseRepository: DatabaseRepository
) {

    suspend operator fun invoke(tokenId: String) = channelFlow<Resource<AuthResult>> {
        authRepository.signInWithGoogle(tokenId).collectLatest { result ->
            when (result) {
                is Resource.Error -> TODO()
                is Resource.Loading -> TODO()
                is Resource.Success -> {
                    val userInfo =
                        databaseRepository.getUserByEmail(result.data?.user?.email.toString())
                    if (userInfo == UserProfile()) {
                        send(Resource.Error("e_google_user_first_time"))
                    } else {
                        send(result)
                    }
                }
            }
        }
    }

}