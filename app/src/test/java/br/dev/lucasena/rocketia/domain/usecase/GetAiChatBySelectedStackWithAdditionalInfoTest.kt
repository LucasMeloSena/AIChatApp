package br.dev.lucasena.rocketia.domain.usecase

import br.dev.lucasena.rocketia.data.createAiChatTextEntityStub
import br.dev.lucasena.rocketia.data.mapper.toDomain
import br.dev.lucasena.rocketia.domain.models.AIChatTextType
import br.dev.lucasena.rocketia.domain.repositories.AIChatRepository
import br.dev.lucasena.rocketia.domain.usecases.AiAdditionalInfo
import br.dev.lucasena.rocketia.domain.usecases.AiAdditionalInfoRepository
import br.dev.lucasena.rocketia.domain.usecases.AiChatTextWithAdditionalInfo
import br.dev.lucasena.rocketia.domain.usecases.GetAiChatBySelectedStackWithAdditionalInfoUseCase
import br.dev.lucasena.rocketia.domain.util.formatDateTime
import br.dev.lucasena.rocketia.domain.util.formatTime
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.Locale

class GetAiChatBySelectedStackWithAdditionalInfoTest {
    private val aiChatRepository = mockk<AIChatRepository>()
    private val aiAdditionalInfo = mockk<AiAdditionalInfoRepository>()

    private lateinit var useCase: GetAiChatBySelectedStackWithAdditionalInfoUseCase

    @Before
    fun setup() {
        useCase = GetAiChatBySelectedStackWithAdditionalInfoUseCase(
            aiChatRepository = aiChatRepository,
            aiAdditionalInfoRepository = aiAdditionalInfo
        )
    }

    @Test
    fun `GIVEN additional info not received WHEN invoked THEN should return same chat conversation`() = runTest {
        val dummyStack = "Kotlin"
        val dummyChatConversation = listOf(
            createAiChatTextEntityStub(
                from = AIChatTextType.ANSWER,
                stack = dummyStack,
                text = "answer"
            )
        ).toDomain()
        val expectedChatTextWithoutAdditionalInfo = AiChatTextWithAdditionalInfo(
            datetime = null,
            chatText = dummyChatConversation.first(),
            additionalInfo = null
        )
        coEvery { aiChatRepository.getAiChatByStack(stack = any()) } returns dummyChatConversation
        coEvery { aiAdditionalInfo.getAdditionalInfo(answer = any()) } returns null

        val result = useCase(stack = dummyStack)
        val firstItem = result[0]
        assertEquals(1, result.size)
        assertEquals(expectedChatTextWithoutAdditionalInfo, firstItem)
    }

    @Test
    fun `GIVEN additional info received WHEN invoked THEN should return same updated chat conversation`() = runTest {
        val dummyStack = "Kotlin"
        val dummyChatConversation = listOf(
            createAiChatTextEntityStub(
                from = AIChatTextType.ANSWER,
                stack = dummyStack,
                text = "answer"
            )
        ).toDomain()
        val dummyLocale = Locale.forLanguageTag("pt-BR")
        val expectedAdditionalInfo = AiAdditionalInfo(
            datetime = 1756295438711L,
            text = "additional info"
        )

        val expectedChatTextWithoutAdditionalInfo = AiChatTextWithAdditionalInfo(
            datetime = expectedAdditionalInfo.datetime.formatDateTime(locale = dummyLocale),
            chatText = dummyChatConversation.first(),
            additionalInfo = expectedAdditionalInfo
        )
        coEvery { aiChatRepository.getAiChatByStack(stack = any()) } returns dummyChatConversation
        coEvery { aiAdditionalInfo.getAdditionalInfo(answer = any()) } returns expectedAdditionalInfo

        val result = useCase(stack = dummyStack)
        val firstItem = result[0]
        assertEquals(1, result.size)
        assertNotNull(expectedChatTextWithoutAdditionalInfo.additionalInfo)
        assertEquals(expectedChatTextWithoutAdditionalInfo, firstItem)
    }

    @Test
    fun `GIVEN just user question chat text WHEN invoked THEN should return same chat conversation`() = runTest {
        val dummyStack = "Kotlin"
        val dummyChatConversation = listOf(
            createAiChatTextEntityStub(
                from = AIChatTextType.USER_QUESTION,
                stack = dummyStack,
                text = "answer"
            )
        ).toDomain()
        val expectedChatTextWithoutAdditionalInfo = AiChatTextWithAdditionalInfo(
            datetime = null,
            chatText = dummyChatConversation.first(),
            additionalInfo = null
        )
        coEvery { aiChatRepository.getAiChatByStack(stack = any()) } returns dummyChatConversation
        coEvery { aiAdditionalInfo.getAdditionalInfo(answer = any()) } returns null

        val result = useCase(stack = dummyStack)
        val firstItem = result[0]
        assertEquals(1, result.size)
        assertEquals(expectedChatTextWithoutAdditionalInfo, firstItem)
    }
}