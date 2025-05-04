package br.dev.lucasena.rocketia.data.datasources

import br.dev.lucasena.rocketia.data.remote.AiService
import br.dev.lucasena.rocketia.di.IODispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AiChatRemoteDataSourceImpl @Inject constructor (
    @IODispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val aiService: AiService
): AiChatRemoteDataSource {
    override suspend fun sendPropmt(stack: String, question: String): String? =
        withContext(ioDispatcher) {
            aiService.sendPropmt(stack, question)
        }
}