package br.dev.lucasena.rocketia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.lucasena.rocketia.domain.models.AIChatText
import br.dev.lucasena.rocketia.domain.usecases.GetAIChatHistoryByStackUseCase
import br.dev.lucasena.rocketia.domain.usecases.GetSelectedStackUseCase
import br.dev.lucasena.rocketia.ui.events.ChatHistoryUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatHistoryViewModel @Inject constructor(
    private val getSelectedStackUseCase: GetSelectedStackUseCase,
    private val getAIChatHistoryByStackUseCase: GetAIChatHistoryByStackUseCase
): ViewModel() {
    val selectedStack: StateFlow<String?> = getSelectedStackUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    private val _selectedStackChipId = MutableStateFlow<Int?>(null)
    val selectedStackChipId: StateFlow<Int?> = _selectedStackChipId.asStateFlow()

    private val _chatHistoryByStack: MutableStateFlow<List<AIChatText>> = MutableStateFlow(emptyList())
    val chatHistoryByStack: StateFlow<List<AIChatText>> = _chatHistoryByStack.asStateFlow()

    fun onEvent(event: ChatHistoryUiEvent) {
        when (event) {
            is ChatHistoryUiEvent.SelectedStack -> getChatHistory(
                stackName = event.selectedStackName,
                stackId = event.selectedStackChipId
            )
        }
    }

    private fun getChatHistory(stackName: String, stackId: Int) {
        viewModelScope.launch {
            val chatBySelectedStack = getAIChatHistoryByStackUseCase(stack = stackName)
            _chatHistoryByStack.update { chatBySelectedStack }.also {
                _selectedStackChipId.update { stackId }
            }
        }
    }
}