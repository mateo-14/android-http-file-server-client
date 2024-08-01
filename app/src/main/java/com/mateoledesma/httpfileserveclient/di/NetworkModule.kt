package com.mateoledesma.httpfileserveclient.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mateoledesma.httpfileserveclient.data.network.FileServerApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

const val BASE_URL = "http://192.168.0.18:8080/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideFileServerApi(retrofit: Retrofit): FileServerApi {
        return retrofit.create(FileServerApi::class.java)
    }
}