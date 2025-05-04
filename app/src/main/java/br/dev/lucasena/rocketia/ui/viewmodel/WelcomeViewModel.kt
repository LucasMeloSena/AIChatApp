package br.dev.lucasena.rocketia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.lucasena.rocketia.domain.usecases.GetSelectedStackUseCase
import br.dev.lucasena.rocketia.ui.events.WelcomeUiEvent
import br.dev.lucasena.rocketia.ui.states.WelcomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class WelcomeViewModel @Inject constructor (
    private val getSelectedStackUseCase: GetSelectedStackUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<WelcomeUiState> = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    fun onEvent(event: WelcomeUiEvent) {
        when(event) {
            WelcomeUiEvent.CheckHasSelectedStack -> checkHasSelectedStack()
        }
    }

    private fun checkHasSelectedStack() {
        viewModelScope.launch {
            val hasSelectedStack = getSelectedStackUseCase().firstOrNull() != null
            _uiState.update { currentUiState ->
                currentUiState.copy(hasSelectedStack = hasSelectedStack)
            }
        }
    }
}