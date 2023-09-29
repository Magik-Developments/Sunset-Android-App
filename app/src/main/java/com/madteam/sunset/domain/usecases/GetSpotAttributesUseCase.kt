package com.madteam.sunset.domain.usecases

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.madteam.sunset.data.database.datastore.DatastoreManager
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.data.model.toEntity
import com.madteam.sunset.data.repositories.DatabaseRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetSpotAttributesUseCase @Inject constructor(
    private val repository: DatabaseRepository,
    private val remoteConfig: FirebaseRemoteConfig,
    private val datastoreManager: DatastoreManager
) {

    suspend operator fun invoke(): List<SpotAttribute> {
        val lastUpdateTimestamp = remoteConfig.getString("lastSpotAttributesUpdate")
        val currentTimestamp = System.currentTimeMillis()
        val lastUpdatedLocal = datastoreManager.getLastSpotAttrUpdated().first()

        val spotAttributes = repository.getAllSpotAttributesFromDatabase()

        return if (lastUpdatedLocal != 0L && lastUpdatedLocal < lastUpdateTimestamp.toLong() && spotAttributes.isNotEmpty()) {
            repository.getAllSpotAttributesFromDatabase()
        } else {
            val apiSpotAttributes = repository.getAllSpotAttributesFromApi()
            repository.insertAllSpotAttributesOnDatabase(apiSpotAttributes.map { it.toEntity() })
            datastoreManager.saveLastSpotAttrUpdated(currentTimestamp)
            apiSpotAttributes
        }
    }

}