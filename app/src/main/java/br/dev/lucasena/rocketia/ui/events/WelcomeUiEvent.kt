package br.dev.lucasena.rocketia.ui.events

sealed interface WelcomeUiEvent {
    data object CheckHasSelectedStack: WelcomeUiEvent
}