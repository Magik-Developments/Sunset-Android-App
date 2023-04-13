package com.dgalan.rmultimateapp.login.domain.di

import com.dgalan.rmultimateapp.login.domain.interactor.FirebaseAuthInteractor
import com.dgalan.rmultimateapp.login.domain.interactor.FirebaseAuthInteractorContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LoginDomainModule {

    @Provides
    fun provideFirebaseAuthInteractor(
        firebaseAuthInteractor: FirebaseAuthInteractor
    ): FirebaseAuthInteractorContract = firebaseAuthInteractor
}