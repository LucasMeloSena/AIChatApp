package br.dev.lucasena.rocketia.di

import br.dev.lucasena.rocketia.domain.repositories.AIChatRepository
import br.dev.lucasena.rocketia.domain.usecases.ChangeStackUseCase
import br.dev.lucasena.rocketia.domain.usecases.GetAIChatHistoryByStackUseCase
import br.dev.lucasena.rocketia.domain.usecases.GetSelectedStackUseCase
import br.dev.lucasena.rocketia.domain.usecases.SendUserQuestionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideChangeStackUseCase(
        aiChatRepository: AIChatRepository
    ): ChangeStackUseCase = ChangeStackUseCase(
        repository = aiChatRepository
    )

    @Provides
    fun provideGetAIChatBySelectedStackUseCase(
        aiChatRepository: AIChatRepository
    ): GetAIChatHistoryByStackUseCase = GetAIChatHistoryByStackUseCase(
        repository = aiChatRepository
    )

    @Provides
    fun provideGetSelectedStackUseCase(
        aiChatRepository: AIChatRepository
    ): GetSelectedStackUseCase = GetSelectedStackUseCase(
        repository = aiChatRepository
    )

    @Provides
    fun provideSendUserQuestionUseCase(
        aiChatRepository: AIChatRepository
    ): SendUserQuestionUseCase = SendUserQuestionUseCase(
        repository = aiChatRepository
    )
}