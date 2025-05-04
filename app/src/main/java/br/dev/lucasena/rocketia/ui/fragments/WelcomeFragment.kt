package br.dev.lucasena.rocketia.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import br.dev.lucasena.rocketia.R
import br.dev.lucasena.rocketia.databinding.FragmentWelcomeBinding
import br.dev.lucasena.rocketia.ui.events.WelcomeUiEvent
import br.dev.lucasena.rocketia.ui.viewmodel.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WelcomeFragment : Fragment() {
    private val viewModel: WelcomeViewModel by viewModels()
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onEvent(event = WelcomeUiEvent.CheckHasSelectedStack)
        setupObservers()

        with(binding) {
            btnWelcomeStart.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeFragment_to_chooseStackFragment)
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    uiState.hasSelectedStack?.let { hasSelectedStack ->
                        if (hasSelectedStack) {
                            findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
                        } else {
                            binding.welcomeLoader.visibility = View.GONE
                            binding.llWelcomeContainer.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}