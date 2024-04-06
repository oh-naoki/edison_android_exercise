package jp.speakbuddy.edisonandroidexercise.storage

import androidx.datastore.core.DataStore
import jp.speakbuddy.edisonandroidexercise.FactStore
import jp.speakbuddy.edisonandroidexercise.network.FactResponse
import javax.inject.Inject

class FactLocalDataSource @Inject constructor(
    private val dataStore: DataStore<FactStore>,
) {

    val factList = dataStore.data

    suspend fun saveFact(fact: FactResponse) {
        dataStore.updateData { current ->
            current.toBuilder().setFact(fact.fact).setLength(fact.length).build()
        }
    }
}
