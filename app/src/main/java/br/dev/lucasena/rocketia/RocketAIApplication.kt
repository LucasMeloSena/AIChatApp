package br.dev.lucasena.rocketia

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RocketAIApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}