package com.example.hinjectlogin

/**
 * (9)
 */
data class LoginUiState(
    val id: String,
    val pw: String,
    val userState:UserState = UserState.NONE
)
