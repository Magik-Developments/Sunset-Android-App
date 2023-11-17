package com.madteam.sunset.domain.usecases

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.madteam.sunset.data.database.datastore.DatastoreManager
import com.madteam.sunset.data.model.SpotAttribute
import com.madteam.sunset.data.model.toEntity
import com.madteam.sunset.data.repositories.DatabaseRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetSpotAttributesUseCase @Inject constructor(
    private val repository: DatabaseRepository,
    private val realtimeDatabase: FirebaseDatabase,
    private val datastoreManager: DatastoreManager
) {

    suspend operator fun invoke(): List<SpotAttribute> {
        val lastUpdateTimestampRef = realtimeDatabase.getReference("LAST_UPDATED_SPOT_ATTR")
        val currentTimestamp = System.currentTimeMillis()
        val lastUpdatedLocal = datastoreManager.getLastSpotAttrUpdated().firstOrNull()
        val lastUpdateTimestampSnapshot = lastUpdateTimestampRef.get().await()
        val lastUpdateTimestamp = lastUpdateTimestampSnapshot.value

        try {
            val spotAttributes = repository.getAllSpotAttributesFromDatabase()

            if (lastUpdatedLocal != null && spotAttributes.isNotEmpty() && lastUpdatedLocal > lastUpdateTimestamp.toString()
                    .toLong()
            ) {
                return spotAttributes
            }
        } catch (e: Exception) {
            Log.e("GetSpotAttributesUseCase", "Error: ${e.message}")
        }

        val apiSpotAttributes = repository.getAllSpotAttributesFromApi()
        repository.insertAllSpotAttributesOnDatabase(apiSpotAttributes.map { it.toEntity() })

        datastoreManager.saveLastSpotAttrUpdated(currentTimestamp)

        return apiSpotAttributes
    }

}