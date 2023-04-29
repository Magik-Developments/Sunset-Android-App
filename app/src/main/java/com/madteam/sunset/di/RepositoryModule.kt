package com.madteam.sunset.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.madteam.sunset.repositories.AuthContract
import com.madteam.sunset.repositories.AuthRepository
import com.madteam.sunset.repositories.DatabaseContract
import com.madteam.sunset.repositories.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideFirebaseAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthContract = AuthRepository(firebaseAuth)


    @Provides
    fun provideDatabaseRepository(firestore: FirebaseFirestore): DatabaseContract =
        DatabaseRepository(firestore)

}