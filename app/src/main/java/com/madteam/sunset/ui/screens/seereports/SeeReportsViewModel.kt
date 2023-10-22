package com.madteam.sunset.ui.screens.seereports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madteam.sunset.data.model.Report
import com.madteam.sunset.data.repositories.AuthRepository
import com.madteam.sunset.data.repositories.DatabaseRepository
import com.madteam.sunset.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeeReportsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _reportsList: MutableStateFlow<List<Report>> =
        MutableStateFlow(mutableListOf())
    val reportsList: StateFlow<List<Report>> = _reportsList

    private lateinit var username: String

    init {
        getReportList()
        viewModelScope.launch {
            getUserUsername()
        }
    }

    private fun getUserUsername() {
        viewModelScope.launch {
            authRepository.getCurrentUser()?.let { user ->
                username = databaseRepository.getUserByEmail(user.email!!).username
            }
        }
    }

    private fun getReportList() {
        viewModelScope.launch {
            databaseRepository.getReportsList().collectLatest {
                _reportsList.value = it
            }
        }
    }

    fun assignReport(clickedReport: String) {
        viewModelScope.launch {
            databaseRepository.assignReport(
                reportId = clickedReport,
                username = username
            ).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        getReportList()
                    }

                    else -> {}
                }
            }
        }
    }

    fun deleteReport(clickedReport: String) {
        viewModelScope.launch {
            databaseRepository.deleteReport(
                reportId = clickedReport
            ).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        getReportList()
                    }

                    else -> {}
                }
            }
        }
    }
}