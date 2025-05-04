package br.dev.lucasena.rocketia.data.mapper

import br.dev.lucasena.rocketia.data.local.database.AiChatTextEntity
import br.dev.lucasena.rocketia.domain.models.AIChatText
import br.dev.lucasena.rocketia.domain.models.AIChatTextType

fun AiChatTextEntity.toDomain(): AIChatText =
     when (this.from) {
        AIChatTextType.USER_QUESTION.name -> AIChatText.UserQuestion(question = this.text)
        AIChatTextType.ANSWER.name -> AIChatText.AIAnswer(answer = this.text)
         else -> throw IllegalArgumentException("Invalid from value: ${this.from}")
     }

fun List<AiChatTextEntity>.toDomain(): List<AIChatText> =
    this.map { aiChatTextEntity -> aiChatTextEntity.toDomain() }