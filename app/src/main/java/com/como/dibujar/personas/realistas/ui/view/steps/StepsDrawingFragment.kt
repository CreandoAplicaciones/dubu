package com.como.dibujar.personas.realistas.ui.view.steps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.como.dibujar.personas.realistas.databinding.FragmentStepsDrawingBinding
import com.como.dibujar.personas.realistas.ui.base.BaseFragment
import com.como.dibujar.personas.realistas.ui.common.Utils
import com.como.dibujar.personas.realistas.ui.common.extension.observe
import com.como.dibujar.personas.realistas.ui.view.main.MainActivity
import com.como.dibujar.personas.realistas.ui.view.steps.StepsDrawingViewModel.Event.*


class StepsDrawingFragment : BaseFragment() {

    private val viewModel: StepsDrawingViewModel by viewModels()
    private lateinit var binding: FragmentStepsDrawingBinding
    private val args: StepsDrawingFragmentArgs by navArgs()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStepsDrawingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.init(viewModel)
        viewModel.initFlow(args.idImage)
        viewModel.eventsFlow.observe(viewLifecycleOwner, ::updateUi)
    }

    private fun updateUi(model: StepsDrawingViewModel.Event) {
        when (model) {
            is SetUp -> {
                binding.buttonBack.setOnClickListener { viewModel.didOnClickButtonBack() }
                binding.buttonNext.setOnClickListener { viewModel.didOnClickButtonNext() }
            }
            is ShowImage -> Utils.image(requireContext(), model.image, binding.imageView)
            is ShowLoad -> binding.progressBar2.isVisible = model.isVisible
            is ShowNumberImages -> binding.txtNumberImages.text = getString(model.restId, model.currentNumber, model.numberImages)
            is ShowButtonBack -> binding.buttonBack.isVisible = model.isVisible
            is ShowButtonNext -> binding.buttonNext.isVisible = model.isVisible
        }
    }
}