package br.dev.lucasena.rocketia.di

import br.dev.lucasena.rocketia.data.datasources.AiChatLocalDataSource
import br.dev.lucasena.rocketia.data.datasources.AiChatLocalDataSourceImpl
import br.dev.lucasena.rocketia.data.datasources.AiChatRemoteDataSource
import br.dev.lucasena.rocketia.data.datasources.AiChatRemoteDataSourceImpl
import br.dev.lucasena.rocketia.data.local.preferences.UserSettingsPreferences
import br.dev.lucasena.rocketia.data.local.preferences.UserSettingsPreferencesImpl
import br.dev.lucasena.rocketia.data.remote.AiGeminiServiceImpl
import br.dev.lucasena.rocketia.data.remote.AiService
import br.dev.lucasena.rocketia.data.repository.AIChatRepositoryImpl
import br.dev.lucasena.rocketia.domain.repositories.AIChatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindingsModule {

    @Binds
    @Singleton
    abstract fun bindAiService(
        impl: AiGeminiServiceImpl
    ): AiService

    @Binds
    @Singleton
    abstract fun bindUserSettingsPreferences(
        impl: UserSettingsPreferencesImpl
    ): UserSettingsPreferences

    @Binds
    @Singleton
    abstract fun bindAiChatLocalDataSource(
        impl: AiChatLocalDataSourceImpl
    ): AiChatLocalDataSource

    @Binds
    @Singleton
    abstract fun bindAiChatRemoteDataSource(
        impl: AiChatRemoteDataSourceImpl
    ): AiChatRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAiChatRepository(
        impl: AIChatRepositoryImpl
    ): AIChatRepository
}