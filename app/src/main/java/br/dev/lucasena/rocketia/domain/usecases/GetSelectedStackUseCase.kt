package br.dev.lucasena.rocketia.domain.usecases

import br.dev.lucasena.rocketia.domain.repositories.AIChatRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetSelectedStackUseCase @Inject constructor (
    private val repository: AIChatRepository
) {
    operator fun invoke(): Flow<String?> {
        return repository.selectedStack
    }
}