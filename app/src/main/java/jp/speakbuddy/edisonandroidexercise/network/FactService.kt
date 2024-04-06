package jp.speakbuddy.edisonandroidexercise.network

import kotlinx.serialization.Serializable
import retrofit2.http.GET

interface FactService {
    @GET("fact")
    suspend fun getFact(): FactResponse
}

@Serializable
data class FactResponse(
    val fact: String,
    val length: Int,
)

