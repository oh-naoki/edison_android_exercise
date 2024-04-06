package jp.speakbuddy.edisonandroidexercise.repository

import jp.speakbuddy.edisonandroidexercise.network.FactService
import javax.inject.Inject

class FactRepository @Inject constructor(
    private val factService: FactService,
) {
    suspend fun fetchFact() = factService.getFact()
}
