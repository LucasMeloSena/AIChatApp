package br.dev.lucasena.rocketia.ui.viewmodel

import app.cash.turbine.test
import br.dev.lucasena.rocketia.data.createAiChatTextEntityStub
import br.dev.lucasena.rocketia.data.mapper.toDomain
import br.dev.lucasena.rocketia.domain.models.AIChatTextType
import br.dev.lucasena.rocketia.domain.usecases.GetAIChatHistoryByStackUseCase
import br.dev.lucasena.rocketia.domain.usecases.GetSelectedStackUseCase
import br.dev.lucasena.rocketia.ui.events.ChatHistoryUiEvent
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AiChatHistoryViewModelTest {
    private val getSelectedStackUseCase: GetSelectedStackUseCase = mockk()
    private val getAiChatBySelectedStackUseCase: GetAIChatHistoryByStackUseCase = mockk()

    private lateinit var viewModel: ChatHistoryViewModel

    @Before
    fun setup() {
        every { getSelectedStackUseCase() } returns flowOf("Kotlin")

        viewModel = ChatHistoryViewModel(
            getSelectedStackUseCase = getSelectedStackUseCase,
            getAIChatHistoryByStackUseCase = getAiChatBySelectedStackUseCase
        )
    }

    @Test
    fun `GIVEN empty history WHEN triggers SelectedStack event THEN aiChatHistoryBySelectedStack and stackChipId should be updated`() = runTest {
        val dummySelectedStackName = "Java"
        val dummySelectedStackChipId = 123
        val aiChatHistoryBySelectedStack = listOf(
            createAiChatTextEntityStub(
                from = AIChatTextType.USER_QUESTION,
                stack = dummySelectedStackName
            ),
            createAiChatTextEntityStub(
                from = AIChatTextType.ANSWER,
                stack = dummySelectedStackName
            )
        ).toDomain()

        coEvery { getAiChatBySelectedStackUseCase(stack = dummySelectedStackName) } returns aiChatHistoryBySelectedStack
        viewModel.onEvent(event = ChatHistoryUiEvent.SelectedStack(selectedStackName = dummySelectedStackName, dummySelectedStackChipId))

        viewModel.chatHistoryByStack.test {
            val result = awaitItem()
            assertEquals(aiChatHistoryBySelectedStack, result)
        }
        viewModel.selectedStackChipId.test {
            val result = awaitItem()
            assertEquals(dummySelectedStackChipId, result)
        }
    }
}