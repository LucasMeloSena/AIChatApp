package br.dev.lucasena.rocketia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.lucasena.rocketia.domain.models.AIChatText
import br.dev.lucasena.rocketia.domain.usecases.GetAIChatHistoryByStackUseCase
import br.dev.lucasena.rocketia.domain.usecases.GetSelectedStackUseCase
import br.dev.lucasena.rocketia.domain.usecases.SendUserQuestionUseCase
import br.dev.lucasena.rocketia.ui.events.ChatUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor (
    private val getSelectedStackUseCase: GetSelectedStackUseCase,
    private val getAiChatBySelectedStackUseCase: GetAIChatHistoryByStackUseCase,
    private val sendUserQuestionUseCase: SendUserQuestionUseCase
): ViewModel() {
    val selectedStack: StateFlow<String?> = getSelectedStackUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )
    val aiChatBySelectedStack: StateFlow<List<AIChatText>> = getAiChatBySelectedStackUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun onEvent(event: ChatUiEvent) {
        when(event) {
            is ChatUiEvent.SendUserQuestion -> sendUserQuestion(event.question)
        }
    }

    private fun sendUserQuestion(question: String) {
        viewModelScope.launch {
            sendUserQuestionUseCase(question = question)
        }
    }
}