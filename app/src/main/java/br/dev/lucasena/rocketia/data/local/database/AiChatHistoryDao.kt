package br.dev.lucasena.rocketia.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AiChatHistoryDao {
    @Query("SELECT * FROM aichattextentity WHERE stack = :stack ORDER BY datetime DESC")
    fun getAllByStackFlow(stack: String): Flow<List<AiChatTextEntity>>

    @Query("SELECT * FROM aichattextentity WHERE stack = :stack ORDER BY datetime DESC")
    suspend fun getAllByStack(stack: String): List<AiChatTextEntity>

    @Insert
    suspend fun insertAll(vararg aiChatText: AiChatTextEntity)
}