package br.dev.lucasena.rocketia.domain.usecases

import br.dev.lucasena.rocketia.domain.models.AIChatText
import br.dev.lucasena.rocketia.domain.repositories.AIChatRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAIChatHistoryByStackUseCase @Inject constructor (
    private val repository: AIChatRepository
) {
    operator fun invoke(): Flow<List<AIChatText>> {
        return repository.aiChatBySelectedStack
    }

    suspend operator fun invoke(stack: String): List<AIChatText> {
        return repository.getAiChatByStack(stack = stack)
    }
}