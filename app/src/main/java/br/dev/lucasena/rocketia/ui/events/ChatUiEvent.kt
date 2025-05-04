package br.dev.lucasena.rocketia.ui.events

sealed interface ChatUiEvent {
    data class SendUserQuestion(val question: String): ChatUiEvent
}