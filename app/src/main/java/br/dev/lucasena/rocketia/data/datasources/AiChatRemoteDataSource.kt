package br.dev.lucasena.rocketia.data.datasources

interface AiChatRemoteDataSource {
    suspend fun sendPropmt(stack: String, question: String): String?
}