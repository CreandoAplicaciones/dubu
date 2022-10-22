package com.como.dibujar.personas.realistas.ui.view.drawables

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.como.dibujar.personas.realistas.databinding.FragmentDrawableListBinding
import com.como.dibujar.personas.realistas.ui.base.BaseFragment
import com.como.dibujar.personas.realistas.ui.common.ALL
import com.como.dibujar.personas.realistas.ui.common.BODY
import com.como.dibujar.personas.realistas.ui.common.FACE
import com.como.dibujar.personas.realistas.ui.common.HAND
import com.como.dibujar.personas.realistas.ui.common.extension.observe
import com.como.dibujar.personas.realistas.ui.view.drawables.DrawableListViewModel.Event.*


class DrawableListFragment : BaseFragment() {

    private val viewModel: DrawableListViewModel by viewModels()
    private lateinit var binding: FragmentDrawableListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDrawableListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.init(viewModel)
        viewModel.initFlow()
        viewModel.eventsFlow.observe(viewLifecycleOwner, ::updateUi)
    }

    private fun updateUi(model: DrawableListViewModel.Event) {
        when (model) {
            SetUp -> { binding.iVAll.setOnClickListener { viewModel.didClickAllButton() }
                binding.iVBody.setOnClickListener { viewModel.didClickBodyButton() }
                binding.iVFace.setOnClickListener { viewModel.didClickFaceButton() }
                binding.iVHand.setOnClickListener { viewModel.didClickHandButton() }
            }
            is ShowLoad -> binding.progressBar.isVisible = model.isVisibility
            is ShowListFace -> {
                binding.rVImages.adapter = DrawableAdapter(model.imagesData, viewModel::didSelectedImage)
            }
            is ShowListAll -> {
                binding.rVImages.adapter = DrawableAdapter(model.imagesData, viewModel::didSelectedImage)
            }
            is ShowListBody -> {
                binding.rVImages.adapter = DrawableAdapter(model.imagesData, viewModel::didSelectedImage)
            }
            is ShowListHand -> {
                binding.rVImages.adapter = DrawableAdapter(model.imagesData, viewModel::didSelectedImage)
            }
            is GoToStepsDrawing -> findNavController().navigate(DrawableListFragmentDirections.actionDrawableListFragmentToStepsDrawingFragment(model.id))
            is SelectedAll -> {
                binding.iVBody.setBackgroundColor(binding.iVAll.context.resources.getColor(model.white))
                binding.iVHand.setBackgroundColor(binding.iVAll.context.resources.getColor(model.white))
                binding.iVFace.setBackgroundColor(binding.iVAll.context.resources.getColor(model.white))
                binding.iVAll.background = resources.getDrawable(model.background)
            }
            is SelectedBody -> {
                binding.iVAll.setBackgroundColor(binding.iVAll.context.resources.getColor(model.white))
                binding.iVHand.setBackgroundColor(binding.iVAll.context.resources.getColor(model.white))
                binding.iVFace.setBackgroundColor(binding.iVAll.context.resources.getColor(model.white))
                binding.iVBody.background = resources.getDrawable(model.background)
            }
            is SelectedFace -> {
                binding.iVBody.setBackgroundColor(binding.iVAll.context.resources.getColor(model.white))
                binding.iVHand.setBackgroundColor(binding.iVAll.context.resources.getColor(model.white))
                binding.iVAll.setBackgroundColor(binding.iVAll.context.resources.getColor(model.white))
                binding.iVFace.background = resources.getDrawable(model.background)
            }
            is SelectedHand -> {
                binding.iVBody.setBackgroundColor(binding.iVAll.context.resources.getColor(model.white))
                binding.iVAll.setBackgroundColor(binding.iVAll.context.resources.getColor(model.white))
                binding.iVFace.setBackgroundColor(binding.iVAll.context.resources.getColor(model.white))
                binding.iVHand.background = resources.getDrawable(model.background)
            }
        }
    }
}