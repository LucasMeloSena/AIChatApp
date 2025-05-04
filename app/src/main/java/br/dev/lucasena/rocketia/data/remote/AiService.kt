package br.dev.lucasena.rocketia.data.remote

interface AiService {
    suspend fun sendPropmt(stack: String, question: String): String?
}