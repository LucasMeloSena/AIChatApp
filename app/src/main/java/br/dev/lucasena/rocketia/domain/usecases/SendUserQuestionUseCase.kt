package br.dev.lucasena.rocketia.domain.usecases

import br.dev.lucasena.rocketia.domain.repositories.AIChatRepository
import javax.inject.Inject

class SendUserQuestionUseCase @Inject constructor (
    private val repository: AIChatRepository
) {
    suspend operator fun invoke(question: String): Unit {
        repository.sendUserQuestion(question)
    }
}