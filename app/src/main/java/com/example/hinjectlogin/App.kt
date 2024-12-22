package com.example.hinjectlogin

import android.app.Application

class App : Application() {
    val appContainer:AppContainer = AppContainer(this)
}