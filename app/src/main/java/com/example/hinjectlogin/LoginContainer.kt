package com.example.hinjectlogin

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class LoginContainer(private val appContainer: AppContainer) {

    fun crateUserDataRepository():UserDataRepository{
        return UserDataRepository(
            appContainer.createUserLocalDataSource(),
            appContainer.createUserRemoteDataSource()
        )
    }


    fun createLoginViewModelFactory(): AbstractSavedStateViewModelFactory {
        val userDataRepository = crateUserDataRepository()
        return object : AbstractSavedStateViewModelFactory() {
            override fun <T : ViewModel> create(
                key: String, modelClass: Class<T>, handle: SavedStateHandle
            ): T {
                return LoginViewModel(userDataRepository) as T
            }

        }
    }

}