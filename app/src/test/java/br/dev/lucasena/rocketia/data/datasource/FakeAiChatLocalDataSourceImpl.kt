package br.dev.lucasena.rocketia.data.datasource

import br.dev.lucasena.rocketia.data.datasources.AiChatLocalDataSource
import br.dev.lucasena.rocketia.data.local.database.AiChatTextEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeAiChatLocalDataSourceImpl: AiChatLocalDataSource {
    private val _aiCurrentChatBySelectedStack = MutableStateFlow<List<AiChatTextEntity>>(emptyList())
    private val _selectedStack = MutableStateFlow<String?>(null)

    private val chatConversationList = mutableListOf<AiChatTextEntity>()

    override val aiCurrentChatByStack: Flow<List<AiChatTextEntity>>
        get() = _aiCurrentChatBySelectedStack.asStateFlow()

    override val selectedStack: Flow<String?>
        get() = _selectedStack.asStateFlow()

    override suspend fun insertAiChatConversation(
        question: AiChatTextEntity,
        answer: AiChatTextEntity
    ) {
        chatConversationList.add(question)
        chatConversationList.add(answer)
        _aiCurrentChatBySelectedStack.value = chatConversationList
    }

    override suspend fun changeSelectedStack(stack: String) {
        _selectedStack.value = stack
    }

    override suspend fun getAiChatByStack(stack: String): List<AiChatTextEntity> {
        return chatConversationList.filter { it.stack == stack }
    }

}