package com.madteam.sunset.welcome.domain.di

import com.madteam.sunset.welcome.domain.interactor.FirebaseAuthInteractor
import com.madteam.sunset.welcome.domain.interactor.FirebaseAuthInteractorContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WelcomeDomainModule {

    @Provides
    fun provideFirebaseAuthInteractor(
        firebaseAuthInteractor: FirebaseAuthInteractor
    ): FirebaseAuthInteractorContract = firebaseAuthInteractor
}