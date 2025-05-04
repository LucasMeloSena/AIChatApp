package br.dev.lucasena.rocketia.ui.events

sealed interface ChatHistoryUiEvent {
    data class SelectedStack(val selectedStackName: String, val selectedStackChipId: Int): ChatHistoryUiEvent
}