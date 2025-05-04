package br.dev.lucasena.rocketia.domain.models

sealed class AIChatText {
    data class UserQuestion(val question: String): AIChatText()
    data class AIAnswer(val answer: String): AIChatText()
}