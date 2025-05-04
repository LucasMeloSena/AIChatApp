package br.dev.lucasena.rocketia.data.datasources

import br.dev.lucasena.rocketia.data.local.database.AiChatTextEntity
import kotlinx.coroutines.flow.Flow

interface AiChatLocalDataSource {

    val aiCurrentChatByStack: Flow<List<AiChatTextEntity>>
    val selectedStack: Flow<String?>

    suspend fun insertAiChatConversation(question: AiChatTextEntity, answer: AiChatTextEntity)
    suspend fun changeSelectedStack(stack: String)
    suspend fun getAiChatByStack(stack: String): List<AiChatTextEntity>
}