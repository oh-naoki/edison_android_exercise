package jp.speakbuddy.edisonandroidexercise.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.speakbuddy.edisonandroidexercise.network.FactService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRepositoryService(): Retrofit =
        Retrofit.Builder().baseUrl("https://catfact.ninja/").addConverterFactory(Json.asConverterFactory("application/json".toMediaType())).build()

    @Provides
    fun provideFactService(retrofit: Retrofit): FactService = retrofit.create(FactService::class.java)
}
