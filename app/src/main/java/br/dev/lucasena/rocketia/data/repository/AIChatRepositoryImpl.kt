package br.dev.lucasena.rocketia.data.repository

import br.dev.lucasena.rocketia.data.datasources.AiChatLocalDataSource
import br.dev.lucasena.rocketia.data.datasources.AiChatRemoteDataSource
import br.dev.lucasena.rocketia.data.local.database.AiChatTextEntity
import br.dev.lucasena.rocketia.data.mapper.toDomain
import br.dev.lucasena.rocketia.domain.models.AIChatText
import br.dev.lucasena.rocketia.domain.models.AIChatTextType
import br.dev.lucasena.rocketia.domain.repositories.AIChatRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AIChatRepositoryImpl @Inject constructor (
    private val aiChatLocalDataSource: AiChatLocalDataSource,
    private val aiChatRemoteDataSource: AiChatRemoteDataSource
): AIChatRepository {
    override val selectedStack: Flow<String?>
        get() = aiChatLocalDataSource.selectedStack
    override val aiChatBySelectedStack: Flow<List<AIChatText>>
        get() = aiChatLocalDataSource.aiCurrentChatByStack.map { currentChatEntity ->
            currentChatEntity.toDomain()
        }

    override suspend fun sendUserQuestion(question: String) {
        val currentStack = selectedStack.first().orEmpty()
        val answer = aiChatRemoteDataSource.sendPropmt(question, currentStack)
        answer?.let {
            aiChatLocalDataSource.insertAiChatConversation(
                question = AiChatTextEntity(
                    stack = currentStack,
                    text = question,
                    from = AIChatTextType.USER_QUESTION.name,
                    datetime = System.currentTimeMillis()
                ),
                answer = AiChatTextEntity(
                    stack = currentStack,
                    text = answer,
                    from = AIChatTextType.ANSWER.name,
                    datetime = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun changeStack(stack: String) {
        aiChatLocalDataSource.changeSelectedStack(stack)
    }

    override suspend fun getAiChatByStack(stack: String): List<AIChatText> =
        aiChatLocalDataSource.getAiChatByStack(stack).toDomain()
}