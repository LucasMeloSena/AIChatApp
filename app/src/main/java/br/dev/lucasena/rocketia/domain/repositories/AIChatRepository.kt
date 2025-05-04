package br.dev.lucasena.rocketia.domain.repositories

import br.dev.lucasena.rocketia.domain.models.AIChatText
import kotlinx.coroutines.flow.Flow

interface AIChatRepository {
    val selectedStack: Flow<String?>
    val aiChatBySelectedStack: Flow<List<AIChatText>>

    suspend fun sendUserQuestion(question: String)
    suspend fun changeStack(stack: String)
    suspend fun getAiChatByStack(stack: String): List<AIChatText>
}