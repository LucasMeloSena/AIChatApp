package br.dev.lucasena.rocketia.data.datasource

import br.dev.lucasena.rocketia.data.datasources.AiChatRemoteDataSource

class FakeAiChatRemoteDataSourceImpl(
    private val shouldEmitError: Boolean = false,
    private val shouldEmitNull: Boolean = false
) : AiChatRemoteDataSource {
    override suspend fun sendPropmt(stack: String, question: String): String? {
        return if (shouldEmitError) throw Exception("HTTP exception")
        else if (shouldEmitNull) {
            null
        } else "answer for questions $question from stack $stack"
    }
}