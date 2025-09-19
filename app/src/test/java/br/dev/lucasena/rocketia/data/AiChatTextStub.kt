package br.dev.lucasena.rocketia.data

import br.dev.lucasena.rocketia.data.local.database.AiChatTextEntity
import br.dev.lucasena.rocketia.domain.models.AIChatTextType

fun createAiChatTextEntityStub(from: AIChatTextType?, text: String = "text", stack: String = "stack"): AiChatTextEntity   {
    return AiChatTextEntity(
        from = from?.name ?: "UNKNOWN",
        stack = stack,
        datetime = 0L,
        text = text
    )
}