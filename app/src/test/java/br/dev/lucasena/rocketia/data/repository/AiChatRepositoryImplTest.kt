package br.dev.lucasena.rocketia.data.repository

import app.cash.turbine.test
import br.dev.lucasena.rocketia.data.createAiChatTextEntityStub
import br.dev.lucasena.rocketia.data.datasource.FakeAiChatLocalDataSourceImpl
import br.dev.lucasena.rocketia.data.datasource.FakeAiChatRemoteDataSourceImpl
import br.dev.lucasena.rocketia.data.datasources.AiChatLocalDataSource
import br.dev.lucasena.rocketia.data.datasources.AiChatRemoteDataSource
import br.dev.lucasena.rocketia.domain.models.AIChatTextType
import br.dev.lucasena.rocketia.domain.repositories.AIChatRepository
import io.mockk.coVerify
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AiChatRepositoryImplTest {
    private lateinit var repository: AIChatRepository
    private lateinit var localDataSource: AiChatLocalDataSource
    private lateinit var remoteDataSource: AiChatRemoteDataSource

    fun setup(
        spyLocal: Boolean = false,
        spyRemote: Boolean = false,
        shouldEmitAiAnswerNull: Boolean = false,
        shouldEmitAiAnswerError: Boolean = false
    ) {
        val fakeRemoteDataSourceImplInstance = FakeAiChatRemoteDataSourceImpl(
            shouldEmitNull = shouldEmitAiAnswerNull,
            shouldEmitError = shouldEmitAiAnswerError
        )

        localDataSource =
            if (spyLocal) spyk(FakeAiChatLocalDataSourceImpl()) else FakeAiChatLocalDataSourceImpl()
        remoteDataSource =
            if (spyRemote) spyk(fakeRemoteDataSourceImplInstance) else fakeRemoteDataSourceImplInstance

        repository = AIChatRepositoryImpl(
            aiChatLocalDataSource = localDataSource,
            aiChatRemoteDataSource = remoteDataSource
        )
    }

    @Test
    fun `GIVEN Ai remote answer is null WHEN send user request THEN should not insert chat conversation`() =
        runTest {
            setup(
                spyLocal = true,
                shouldEmitAiAnswerNull = true,
            )
            val dummyQuestion = "question"
            repository.sendUserQuestion(question = dummyQuestion)
            coVerify(exactly = 0) {
                localDataSource.insertAiChatConversation(
                    question = any(),
                    answer = any()
                )
            }
            repository.aiChatBySelectedStack.test {
                val result = awaitItem()
                assert(result.isEmpty())
            }
        }

    @Test
    fun `GIVEN Ai remote answer is not null WHEN send user request THEN should insert chat conversation`() =
        runTest {
            setup(
                spyLocal = true,
            )
            val dummyQuestion = "question"
            repository.sendUserQuestion(question = dummyQuestion)
            coVerify(exactly = 1) {
                localDataSource.insertAiChatConversation(
                    question = any(),
                    answer = any()
                )
            }
            repository.aiChatBySelectedStack.test {
                val result = awaitItem()
                assertEquals(2, result.size)
            }
        }

    @Test
    fun `GIVEN change selected stack WHEN executed THEN should change selected stack `() = runTest {
        setup()
        val dummyStack = "Kotlin"
        repository.changeStack(stack = dummyStack)
        repository.selectedStack.test {
            val result = awaitItem()
            assertEquals(dummyStack, result)
        }
    }

    @Test
    fun `GIVEN get chat conversation by stack WHEN chat conversation is not empty THEN should return chat conversation by stack`() =
        runTest {
            setup()

            val dummyStack = "Kotlin"
            val dummyChatConversation = listOf(
                createAiChatTextEntityStub(
                    from = AIChatTextType.USER_QUESTION,
                    text = "question",
                    stack = dummyStack
                ),
                createAiChatTextEntityStub(
                    from = AIChatTextType.ANSWER,
                    stack = dummyStack,
                    text = "answer"
                )
            )
            localDataSource.insertAiChatConversation(
                question = dummyChatConversation.first(),
                answer = dummyChatConversation[1]
            )
            val result = repository.getAiChatByStack(stack = dummyStack)
            assertEquals(2, result.size)
        }

    @Test
    fun `GIVEN get chat conversation by stack WHEN is empty THEN should return empty chat conversation`() =
        runTest {
            setup()

            val dummyStack = "Swift"
            val dummyChatConversation = listOf(
                createAiChatTextEntityStub(
                    from = AIChatTextType.USER_QUESTION,
                    text = "question",                ),
                createAiChatTextEntityStub(
                    from = AIChatTextType.ANSWER,
                    text = "answer"
                )
            )
            localDataSource.insertAiChatConversation(
                question = dummyChatConversation.first(),
                answer = dummyChatConversation[1]
            )
            val result = repository.getAiChatByStack(stack = dummyStack)
            assertEquals(0, result.size)
        }
}