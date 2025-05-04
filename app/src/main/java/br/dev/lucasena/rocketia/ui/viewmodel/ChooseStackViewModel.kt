package br.dev.lucasena.rocketia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.lucasena.rocketia.domain.usecases.ChangeStackUseCase
import br.dev.lucasena.rocketia.ui.events.ChooseStackUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseStackViewModel @Inject constructor(
    private val changeStackUseCase: ChangeStackUseCase
): ViewModel() {
    private val _selectedStackChipId = MutableStateFlow<Int?>(null)
    val selectedStackChipId: StateFlow<Int?> = _selectedStackChipId.asStateFlow()

    private val _isConfirmedNewStack = MutableStateFlow<Boolean>(false)
    val isConfirmedNewStack: StateFlow<Boolean> = _isConfirmedNewStack.asStateFlow()

    fun onEvent(event: ChooseStackUiEvent) {
        when (event) {
            is ChooseStackUiEvent.SelectedStack -> selectStack(
                selectedStackName = event.selectedStackName,
                selectedStackChipId = event.selectedStackChipId
            )
            else -> throw IllegalArgumentException("Invalid type")
        }
    }

    private fun selectStack(selectedStackName: String, selectedStackChipId: Int) {
        _isConfirmedNewStack.update { true }
        viewModelScope.launch {
            changeStackUseCase(stack = selectedStackName)
        }
        _selectedStackChipId.update { selectedStackChipId }
    }
}