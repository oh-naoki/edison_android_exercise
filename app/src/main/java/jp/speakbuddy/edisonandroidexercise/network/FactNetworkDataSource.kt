package jp.speakbuddy.edisonandroidexercise.network

import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FactNetworkDataSource @Inject constructor(
    private val factService: FactService,
) {
    fun fetchFact() = flow { emit(factService.getFact()) }
}
