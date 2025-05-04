package br.dev.lucasena.rocketia.data.datasources

import br.dev.lucasena.rocketia.data.local.database.AiChatHistoryDao
import br.dev.lucasena.rocketia.data.local.database.AiChatTextEntity
import br.dev.lucasena.rocketia.data.local.preferences.UserSettingsPreferences
import br.dev.lucasena.rocketia.di.IODispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AiChatLocalDataSourceImpl @Inject constructor (
    @IODispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val aiChatHistoryDao: AiChatHistoryDao,
    private val userSettingsPreferences: UserSettingsPreferences
): AiChatLocalDataSource {
    override val aiCurrentChatByStack: Flow<List<AiChatTextEntity>>
        get() = userSettingsPreferences.selectedStack.flatMapLatest { selectedStack ->
                aiChatHistoryDao.getAllByStackFlow(selectedStack.orEmpty())
        }.flowOn(ioDispatcher)

    override val selectedStack: Flow<String?>
        get() = userSettingsPreferences.selectedStack.flowOn(ioDispatcher)

    override suspend fun insertAiChatConversation(
        question: AiChatTextEntity,
        answer: AiChatTextEntity
    ) {
        withContext(ioDispatcher) {
            aiChatHistoryDao.insertAll(question, answer)
        }
    }

    override suspend fun changeSelectedStack(stack: String) {
        withContext(ioDispatcher) {
            userSettingsPreferences.changeSelectedStack(stack)
        }
    }

    override suspend fun getAiChatByStack(stack: String): List<AiChatTextEntity> =
        withContext(ioDispatcher) {
            aiChatHistoryDao.getAllByStack(stack = stack)
        }
}