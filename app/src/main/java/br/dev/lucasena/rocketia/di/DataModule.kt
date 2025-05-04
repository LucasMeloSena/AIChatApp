package br.dev.lucasena.rocketia.di

import android.content.Context
import androidx.room.Room
import br.dev.lucasena.rocketia.data.local.database.AiChatHistoryDao
import br.dev.lucasena.rocketia.data.local.database.ROCKET_AI_DATABASE_NAME
import br.dev.lucasena.rocketia.data.local.database.RocketAiDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideRocketAIDatabase(
        @ApplicationContext context: Context
    ): RocketAiDatabase = Room.databaseBuilder(
        context,
        RocketAiDatabase::class.java,
        ROCKET_AI_DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideAIChatHistoryDao(
        rocketAIDatabase: RocketAiDatabase
    ): AiChatHistoryDao = rocketAIDatabase.aiChatHistoryDao()

    @Provides
    @Singleton
    @IODispatcher
    fun provideIODispatcher() = Dispatchers.IO
}