package br.dev.lucasena.rocketia.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import br.dev.lucasena.rocketia.R
import br.dev.lucasena.rocketia.databinding.FragmentChatBinding
import br.dev.lucasena.rocketia.ui.adapter.ChatAdapter
import br.dev.lucasena.rocketia.ui.events.ChatUiEvent
import br.dev.lucasena.rocketia.ui.extension.*
import br.dev.lucasena.rocketia.ui.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private val viewModel: ChatViewModel by viewModels()
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        with(binding) {
            clMainContainer.setOnClickListener {
                clearQuestionTextInput()
            }
            rvChatConversations.adapter = ChatAdapter()

            val userSettingsPopMenu = PopupMenu(requireContext(), ibSettings)
            userSettingsPopMenu.setupUserSettingsPopMenu()
            ibSettings.setOnClickListener {
                userSettingsPopMenu.show()
            }

            tietUserQuestion.doOnTextChanged { _, _, _, _ ->
                if (tilQuestion.error != null) {
                    tilQuestion.error = null
                }
            }

            btnSendQuestionToAi.setOnClickListener {
                val userQuestion = tietUserQuestion.text.toString()
                if (userQuestion.isNotEmpty()) {
                    showLoadingAnswer()
                    clearQuestionTextInput()

                    viewModel.onEvent(ChatUiEvent.SendUserQuestion(
                        question = userQuestion
                    ))
                } else {
                    tilQuestion.error = "Campo Obrigatório"
                }
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.selectedStack.collect { selectedStack ->
                        selectedStack?.let {
                            binding.tvStack.text = getString(R.string.ola_dev, selectedStack)
                            binding.tilQuestion.hint = getString(R.string.qual_a_sua_duvida_sobre, selectedStack)
                        }
                    }
                }
                launch {
                    viewModel.aiChatBySelectedStack.collect { aiChatBySelectedStack ->
                        val chatAdapter = binding.rvChatConversations.adapter as? ChatAdapter
                        chatAdapter?.apply {
                            submitList(aiChatBySelectedStack)
                            binding.rvChatConversations.smoothScrollToPosition(0)
                            delay(200)
                            binding.showLoadedAnswer()
                        }
                    }
                }
            }
        }
    }

    private fun PopupMenu.setupUserSettingsPopMenu() {
        this.menuInflater.inflate(R.menu.user_settings_menu, this.menu)
        this.setOnMenuItemClickListener { itemMenu ->
            when (itemMenu.itemId) {
                R.id.action_change_stack  -> {
                    requireActivity().findNavController(R.id.fcvMainContainer).navigate(R.id.action_homeFragment_to_chooseStackFragment)
                    true
                } else -> false
            }
        }
    }

    private fun FragmentChatBinding.clearQuestionTextInput() {
        tilQuestion.clearFocus()
        tietUserQuestion.text = null
        this.root.hideKeyboard()
    }

    private fun FragmentChatBinding.showLoadingAnswer() {
        chatLoader.visible()
        rvChatConversations.gone()
    }

    private fun FragmentChatBinding.showLoadedAnswer() {
        chatLoader.gone()
        rvChatConversations.visible()
    }
}