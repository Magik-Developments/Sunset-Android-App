package com.madteam.sunset.domain.usecases

import com.google.firebase.auth.AuthResult
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.utils.Resource
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(tokenId: String) = channelFlow<Resource<AuthResult>> {
        authRepository.signInWithGoogle(tokenId).collectLatest { result ->
            when (result) {
                is Resource.Error -> TODO()
                is Resource.Loading -> TODO()
                is Resource.Success -> {
                    authRepository.checkIfUserEmailExistsInDatabase(result.data?.user?.email.toString())
                        .collectLatest {
                            when (it) {
                                is Resource.Error -> {
                                    //
                                }

                                is Resource.Loading -> {

                                }

                                is Resource.Success -> {
                                    if (it.data == true) {
                                        send(result)
                                    } else {
                                        //
                                    }
                                }
                            }
                        }
                }
            }
        }
    }

}