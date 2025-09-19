package br.dev.lucasena.rocketia.data.mapper

import br.dev.lucasena.rocketia.data.createAiChatTextEntityStub
import br.dev.lucasena.rocketia.domain.models.AIChatText
import br.dev.lucasena.rocketia.domain.models.AIChatTextType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class AiChatHistoryMapperTest {
    @Test
    fun `Given AIChatTextEntity from User Question type WHEN toDomain is called THEN should convert to AiChatText from User Question`() =
        runTest {
            val userQuestionEntity = createAiChatTextEntityStub(
                from = AIChatTextType.USER_QUESTION,
            )
            val result = userQuestionEntity.toDomain()
            assertTrue(result is AIChatText.UserQuestion)
            assertEquals(userQuestionEntity.text, (result as AIChatText.UserQuestion).question)
        }

    @Test
    fun `Given AIChatTextEntity from Ai Answer type WHEN toDomain is called THEN should convert to AiChatText from Ai Answer`() =
        runTest {
            val aiAnswerEntity = createAiChatTextEntityStub(
                from = AIChatTextType.ANSWER,
            )
            val result = aiAnswerEntity.toDomain()
            assertTrue(result is AIChatText.AIAnswer)
            assertEquals(aiAnswerEntity.text, (result as AIChatText.AIAnswer).answer)
        }

    @Test
    fun `Given AIChatTextEntity from Unknown type WHEN toDomain is called THEN should trigger an exception`() =
        runTest {
            val unknownEntity = createAiChatTextEntityStub(
                from = null,
            )
            val result = assertThrows(IllegalArgumentException::class.java) {
                unknownEntity.toDomain()
            }
            assertEquals("Invalid from value: UNKNOWN", result.message)
        }

    @Test
    fun `Given AIChatTextEntity list WHEN toDomain is called THEN should convert to AiChatText list`() =
        runTest {
            val aiChatTextEntityList = List(10) { index ->
                createAiChatTextEntityStub(
                    from = when {
                        index % 2 == 0 -> AIChatTextType.USER_QUESTION
                        index % 2 != 0 -> AIChatTextType.ANSWER
                        else -> null
                    }
                )
            }
            val result = aiChatTextEntityList.toDomain()
            assertEquals(aiChatTextEntityList.size, result.size)
        }
}