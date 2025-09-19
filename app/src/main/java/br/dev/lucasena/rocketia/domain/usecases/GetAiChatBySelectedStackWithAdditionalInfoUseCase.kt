package br.dev.lucasena.rocketia.domain.usecases

import br.dev.lucasena.rocketia.domain.models.AIChatText
import br.dev.lucasena.rocketia.domain.repositories.AIChatRepository
import br.dev.lucasena.rocketia.domain.util.formatDateTime

data class AiChatTextWithAdditionalInfo (
    val datetime: String?,
    val chatText: AIChatText,
    val additionalInfo: AiAdditionalInfo?
)

data class AiAdditionalInfo (
    val datetime: Long,
    val text: String?
)

interface AiAdditionalInfoRepository {
    fun getAdditionalInfo(answer: String): AiAdditionalInfo?
}

class GetAiChatBySelectedStackWithAdditionalInfoUseCase(
    private val aiChatRepository: AIChatRepository,
    private val aiAdditionalInfoRepository: AiAdditionalInfoRepository
) {
    suspend operator fun invoke(stack: String): List<AiChatTextWithAdditionalInfo> {
        return aiChatRepository.getAiChatByStack(stack = stack).map { aiChatText ->
            when (aiChatText) {
                is AIChatText.AIAnswer -> {
                    val additionalInfo = aiAdditionalInfoRepository.getAdditionalInfo(answer = aiChatText.answer)
                    AiChatTextWithAdditionalInfo(
                        datetime = additionalInfo?.datetime?.formatDateTime(),
                        chatText = aiChatText,
                        additionalInfo = additionalInfo
                    )
                }
                else -> {
                    AiChatTextWithAdditionalInfo(
                        datetime = null,
                        chatText = aiChatText,
                        additionalInfo = null
                    )
                }
            }
        }
    }
}