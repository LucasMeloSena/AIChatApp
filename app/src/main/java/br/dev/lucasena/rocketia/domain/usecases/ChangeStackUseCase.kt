package br.dev.lucasena.rocketia.domain.usecases

import br.dev.lucasena.rocketia.domain.repositories.AIChatRepository
import javax.inject.Inject

class ChangeStackUseCase @Inject constructor (
    private val repository: AIChatRepository
) {
    suspend operator fun invoke(stack: String): Unit {
        repository.changeStack(stack)
    }
}