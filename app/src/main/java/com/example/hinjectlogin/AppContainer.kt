package com.example.hinjectlogin

import android.content.Context
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create

class AppContainer(val context : Context) {

    fun createRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://localhost:8080/api/")
            .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .build()
    }

    fun createLoginRetrofitService(): LoginRetrofitService {
        return createRetrofit().create(LoginRetrofitService::class.java)
    }

    fun createUserLocalDataSource():UserLocalDataSource {
        return UserLocalDataSource(context)
    }

    fun createUserRemoteDataSource():UserRemoteDataSource{
        return UserRemoteDataSource(createLoginRetrofitService())
    }

    var loginContainer : LoginContainer? = null


}