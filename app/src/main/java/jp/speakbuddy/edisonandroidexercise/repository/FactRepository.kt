package jp.speakbuddy.edisonandroidexercise.repository

import jp.speakbuddy.edisonandroidexercise.network.FactNetworkDataSource
import jp.speakbuddy.edisonandroidexercise.network.FactResponse
import jp.speakbuddy.edisonandroidexercise.storage.FactLocalDataSource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class FactRepository @Inject constructor(
    private val factNetworkDataSource: FactNetworkDataSource,
    private val factLocalDataSource: FactLocalDataSource,
) {
    fun fetchFact() = factNetworkDataSource.fetchFact().onEach {
        factLocalDataSource.saveFact(it)
    }.catch {
        val fact = factLocalDataSource.factList.first()
        emit(
            FactResponse(
                fact = fact.fact, length = fact.length
            )
        )
    }
}
