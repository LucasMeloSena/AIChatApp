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
import androidx.navigation.fragment.findNavController
import br.dev.lucasena.rocketia.R
import br.dev.lucasena.rocketia.databinding.FragmentChooseStackBinding
import br.dev.lucasena.rocketia.domain.models.TechStack
import br.dev.lucasena.rocketia.ui.events.ChooseStackUiEvent
import br.dev.lucasena.rocketia.ui.events.WelcomeUiEvent
import br.dev.lucasena.rocketia.ui.viewmodel.ChooseStackViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChooseStackFragment : Fragment() {
    private val viewModel: ChooseStackViewModel by viewModels()

    private var _binding: FragmentChooseStackBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseStackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadChips()
        setupObservers()
        with(binding) {
            setupStackChips()
            btnConfirm.setOnClickListener {
                findNavController().navigate(R.id.action_chooseStackFragment_to_homeFragment)
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                 launch {
                     viewModel.selectedStackChipId.collect { selectedStackChipId ->
                         selectedStackChipId?.let {
                             binding.changeSelectedStack(selectedStackChipId)
                         }
                     }
                 }

                 launch {
                     viewModel.isConfirmedNewStack.collect { isConfirmedNewStack ->
                         binding.btnConfirm.isEnabled = isConfirmedNewStack
                     }
                 }
             }
         }
    }

    private fun FragmentChooseStackBinding.setupStackChips() {
        flow.referencedIds.forEach { id ->
            val stackChip = root.findViewById<Chip>(id)

            stackChip.setOnClickListener {
                viewModel.onEvent(event = ChooseStackUiEvent.SelectedStack(
                    selectedStackName = stackChip.text.toString(),
                    selectedStackChipId = id
                ))
            }
        }
    }

    private fun FragmentChooseStackBinding.changeSelectedStack(stackChipId: Int) {
        flow.referencedIds.forEach { id ->
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
                val chip = LayoutInflater.from(context).inflate(R.layout.item_chip, rootLayout, false) as Chip

                chip.id = View.generateViewId()
                chip.text = tech.name
                chip.chipIcon = ContextCompat.getDrawable(context, tech.image)

                rootLayout.addView(chip)
                chipIds.add(chip.id)
            }

            flow.referencedIds = chipIds.toIntArray()
        }
    }
}