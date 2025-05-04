package br.dev.lucasena.rocketia.ui.events

sealed interface ChooseStackUiEvent {
    data class SelectedStack(val selectedStackName: String, val selectedStackChipId: Int): ChooseStackUiEvent
}