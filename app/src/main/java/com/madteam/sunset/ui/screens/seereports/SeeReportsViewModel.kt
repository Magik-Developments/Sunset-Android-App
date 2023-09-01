package com.madteam.sunset.ui.screens.seereports

import androidx.lifecycle.ViewModel
import com.madteam.sunset.repositories.AuthRepository
import com.madteam.sunset.repositories.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SeeReportsViewModel @Inject constructor(
    databaseRepository: DatabaseRepository,
    authRepository: AuthRepository
) : ViewModel()