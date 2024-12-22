package com.example.hinjectlogin

import kotlinx.serialization.Serializable

/**
 * (3)
 */
@Serializable
data class LoginParam(
    val loginId: String,
    val password: String
)