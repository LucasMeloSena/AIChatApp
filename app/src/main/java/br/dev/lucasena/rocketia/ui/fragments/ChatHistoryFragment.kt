package br.dev.lucasena.rocketia.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.dev.lucasena.rocketia.R
import br.dev.lucasena.rocketia.databinding.FragmentChatBinding
import br.dev.lucasena.rocketia.databinding.FragmentChatHistoryBinding
import br.dev.lucasena.rocketia.databinding.FragmentChooseStackBinding
import br.dev.lucasena.rocketia.domain.models.TechStack
import br.dev.lucasena.rocketia.ui.adapter.ChatAdapter
import br.dev.lucasena.rocketia.ui.events.ChatHistoryUiEvent
import br.dev.lucasena.rocketia.ui.events.ChooseStackUiEvent
import br.dev.lucasena.rocketia.ui.viewmodel.ChatHistoryViewModel
import br.dev.lucasena.rocketia.ui.viewmodel.ChatViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.forEach
import kotlin.getValue

@AndroidEntryPoint
class ChatHistoryFragment : Fragment() {
    private val viewModel: ChatHistoryViewModel by viewModels()
    private var _binding: FragmentChatHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadChips()

        setupOpbservers()

        with(binding) {
            setupStackChips()
            rvHistoryChat.adapter = ChatAdapter()
        }
    }

    private fun setupOpbservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.selectedStack.collect { selectedStack ->
                        selectedStack?.let {
                            val selectedStackChipId = binding.getStackChipId(selectedStack)
                            selectedStackChipId?.let {
                                viewModel.onEvent(
                                    ChatHistoryUiEvent.SelectedStack(
                                        selectedStackName = selectedStack,
                                        selectedStackChipId = selectedStackChipId
                                    )
                                )
                            }
                        }
                    }
                }

                launch {
                    viewModel.chatHistoryByStack.collect { chatHistoryByStack ->
                        val chatAdapter = binding.rvHistoryChat.adapter as? ChatAdapter
                        chatAdapter?.apply {
                            submitList(chatHistoryByStack)
                            binding.rvHistoryChat.smoothScrollToPosition(0)
                        }
                    }
                }

                launch {
                    viewModel.selectedStackChipId.collect { selectedStackChipId ->
                        selectedStackChipId?.let {
                            binding.changeSelectedStack(selectedStackChipId)
                        }
                    }
                }
            }
        }
    }

    private fun FragmentChatHistoryBinding.changeSelectedStack(stackChipId: Int) {
        flowChatHistory.referencedIds.forEach { id ->
            val stackChip = root.findViewById<Chip>(id)

            stackChip?.apply {
                setChipStrokeColorResource(
                    if (stackChip.id == stackChipId)
                        R.color.white
                    else
                        R.color.border_default
                )
                isChecked = stackChip.id == stackChipId
            }
        }
    }

    private fun FragmentChatHistoryBinding.setupStackChips() {
        flowChatHistory.referencedIds.forEach { id ->
            val stackChip = root.findViewById<Chip>(id)

            stackChip.setOnClickListener {
                viewModel.onEvent(ChatHistoryUiEvent.SelectedStack(
                    selectedStackChipId = id,
                    selectedStackName = stackChip.text.toString()
                ))
            }
        }
    }

    private fun FragmentChatHistoryBinding.getStackChipId(stackName: String): Int? =
        flowChatHistory.referencedIds.find { stackChipId ->
            val stackChip = root.findViewById<Chip>(stackChipId)

            stackChip.text == stackName
        }

    private fun loadChips() {
        val techList: List<TechStack> = listOf(
            TechStack(name="React Native", image= R.drawable.ic_react_native),
            TechStack(name="C#", image= R.drawable.ic_csharp),
            TechStack(name="DevOps", image= R.drawable.ic_devops),
            TechStack(name="FullStack", image= R.drawable.ic_fullstack),
            TechStack(name="Go", image= R.drawable.ic_go),
            TechStack(name="Java", image= R.drawable.ic_java),
            TechStack(name="Kotlin", image= R.drawable.ic_kotlin),
            TechStack(name="Node", image= R.drawable.ic_nodejs),
            TechStack(name="Php", image= R.drawable.ic_php),
            TechStack(name="Python", image= R.drawable.ic_python),
            TechStack(name="React", image= R.drawable.ic_react),
            TechStack(name="Swift", image= R.drawable.ic_swift),
        )
        val chipIds = mutableListOf<Int>()

        with(binding) {
            techList.forEach { tech ->
                val chip = LayoutInflater.from(context).inflate(R.layout.item_chip, clFlow, false) as Chip

                chip.id = View.generateViewId()
                chip.text = tech.name
                chip.chipIcon = ContextCompat.getDrawable(context!!, tech.image)

                clFlow.addView(chip)
                chipIds.add(chip.id)
            }

            flowChatHistory.referencedIds = chipIds.toIntArray()
        }
    }
}