package br.dev.lucasena.rocketia.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

const val ROCKET_AI_DATABASE_NAME = "rocket_ai_db"

@Database(entities = [AiChatTextEntity::class], version = 1)
abstract class RocketAiDatabase: RoomDatabase() {
    abstract fun aiChatHistoryDao(): AiChatHistoryDao
}