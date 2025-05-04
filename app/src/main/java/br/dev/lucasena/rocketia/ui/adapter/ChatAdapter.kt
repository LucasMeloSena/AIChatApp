package br.dev.lucasena.rocketia.ui.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import br.dev.lucasena.rocketia.R
import br.dev.lucasena.rocketia.databinding.ItemAiChatBallonBinding
import br.dev.lucasena.rocketia.databinding.ItemUserChatBallonBinding
import br.dev.lucasena.rocketia.domain.models.AIChatText
import io.noties.markwon.Markwon

private const val AI_ANSWER_CLIP_DATA_LABEL = "Resposta da IA copiada!"

class ChatAdapter: ListAdapter<AIChatText, ChatAdapter.ChatViewHolder>(ChatTextDiffCallback()) {
    class ChatViewHolder(val binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
        private val clipboardManager = binding.root.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        fun bindQuestion(question: String) {
            with(binding as ItemUserChatBallonBinding) {
                tvUserChatBallon.text = question
            }
        }

        fun bindAnswer(answer: String) {
            with(binding as ItemAiChatBallonBinding) {
                val markwon = Markwon.create(binding.root.context)
                markwon.setMarkdown(tvAiChatBallon, answer)
                tvAiChatBallon.setOnLongClickListener {
                    val clipData = ClipData.newPlainText(AI_ANSWER_CLIP_DATA_LABEL , answer)
                    clipboardManager.setPrimaryClip(clipData)
                    Toast.makeText(binding.root.context, "Texto copiado para área de transferência!", Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            R.layout.item_user_chat_ballon -> {
                val userChatBinding = ItemUserChatBallonBinding.inflate(inflater, parent, false)
                ChatViewHolder(userChatBinding)
            }

            R.layout.item_ai_chat_ballon -> {
                val aiChatBinding = ItemAiChatBallonBinding.inflate(inflater, parent, false)
                ChatViewHolder(aiChatBinding)
            }

            else -> throw  IllegalArgumentException("Invalid view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        when (val aiChatText = getItem(position)) {
            is AIChatText.AIAnswer -> holder.bindAnswer(answer = aiChatText.answer)
            is AIChatText.UserQuestion -> holder.bindQuestion(question = aiChatText.question)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is AIChatText.AIAnswer -> R.layout.item_ai_chat_ballon
            is AIChatText.UserQuestion -> R.layout.item_user_chat_ballon
        }
    }
}

class ChatTextDiffCallback: DiffUtil.ItemCallback<AIChatText>() {
    override fun areItemsTheSame(oldItem: AIChatText, newItem: AIChatText): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AIChatText, newItem: AIChatText): Boolean {
        return oldItem == newItem
    }

}