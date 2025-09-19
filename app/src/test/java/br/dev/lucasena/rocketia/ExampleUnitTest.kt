package br.dev.lucasena.rocketia

import android.widget.Toast
import br.dev.lucasena.rocketia.data.datasource.FakeAiChatLocalDataSourceImpl
import br.dev.lucasena.rocketia.data.datasource.FakeAiChatRemoteDataSourceImpl
import br.dev.lucasena.rocketia.data.datasources.AiChatLocalDataSource
import br.dev.lucasena.rocketia.data.datasources.AiChatRemoteDataSource
import br.dev.lucasena.rocketia.data.local.database.AiChatTextEntity
import br.dev.lucasena.rocketia.data.repository.AIChatRepositoryImpl
import br.dev.lucasena.rocketia.domain.models.AIChatTextType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Test
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ExampleUnitTest {
    @Test
    fun `example`() = runTest {
        val fakeAiChatRemoteDataSourceImpl = FakeAiChatRemoteDataSourceImpl()

        val dummyAiChatTextEntity = AiChatTextEntity(
            from = AIChatTextType.USER_QUESTION.name,
            stack = "stack",
            datetime = 0L,
            text = "text"
        )
        val dummyAiChatTextEntityList = listOf(dummyAiChatTextEntity, dummyAiChatTextEntity, dummyAiChatTextEntity)
        val stubAiChatLocalDataSourceImpl = mockk<AiChatLocalDataSource>()
        coEvery { stubAiChatLocalDataSourceImpl.getAiChatByStack(any()) } returns dummyAiChatTextEntityList

        val testRepository = AIChatRepositoryImpl(
            aiChatLocalDataSource = stubAiChatLocalDataSourceImpl,
            aiChatRemoteDataSource = fakeAiChatRemoteDataSourceImpl
        )
        val result = testRepository.getAiChatByStack(stack = "Java")
        coVerify(exactly = 1) { stubAiChatLocalDataSourceImpl.getAiChatByStack("Java") }
        assertEquals(3, result.size)
    }

    @Test
    fun `example_mock_and_spy`() = runTest {
        val fakeAiChatLocalDataSourceImpl = FakeAiChatLocalDataSourceImpl()
        val mockAiChatRemoteDataSourceImpl = mockk<AiChatRemoteDataSource>(relaxed = true)
        val spyAiChatLocalDataSourceImpl = spyk<AiChatLocalDataSource>(fakeAiChatLocalDataSourceImpl)

        val testRepository = AIChatRepositoryImpl(
            aiChatLocalDataSource = spyAiChatLocalDataSourceImpl,
            aiChatRemoteDataSource = mockAiChatRemoteDataSourceImpl
        )

        testRepository.sendUserQuestion("question")

        coVerify(exactly = 1) { mockAiChatRemoteDataSourceImpl.sendPropmt(any(), any()) }
        coVerify(exactly = 1) { spyAiChatLocalDataSourceImpl.insertAiChatConversation(any(), any())  }
    }

    @Test
    fun `example_shadow`() {
        val context = RuntimeEnvironment.getApplication()
        Toast.makeText(context, "Hello World!", Toast.LENGTH_SHORT).show()
        assertEquals("Hello World!", ShadowToast.getTextOfLatestToast())
    }
}