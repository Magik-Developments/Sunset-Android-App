package com.madteam.sunset.welcome.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WelcomeDataModule {

    @Provides
    fun provideFirebaseAuthRepository(
        firebaseAuthRepository: FirebaseAuthRepository
    ): FireBaseAuthRepositoryContract = firebaseAuthRepository
}