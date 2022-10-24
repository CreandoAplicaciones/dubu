package com.como.dibujar.personas.realistas.ui.view.steps

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.como.dibujar.personas.realistas.R
import com.como.dibujar.personas.realistas.databinding.FragmentStepsDrawingBinding
import com.como.dibujar.personas.realistas.ui.base.BaseFragment
import com.como.dibujar.personas.realistas.ui.common.DIFFICULTY
import com.como.dibujar.personas.realistas.ui.common.Utils
import com.como.dibujar.personas.realistas.ui.common.extension.observe
import com.como.dibujar.personas.realistas.ui.view.drawables.DrawableListViewModel
import com.como.dibujar.personas.realistas.ui.view.main.MainActivity
import com.como.dibujar.personas.realistas.ui.view.steps.StepsDrawingViewModel.Event.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class StepsDrawingFragment : BaseFragment() {

    private val viewModel: StepsDrawingViewModel by viewModels()
    private lateinit var binding: FragmentStepsDrawingBinding
    private val args: StepsDrawingFragmentArgs by navArgs()
    private var mInterstitialAd: InterstitialAd? = null
    private val adRequest = AdRequest.Builder().build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStepsDrawingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.init(viewModel)
        activity?.let { activity ->  activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)}
        viewModel.initFlow(args.idImage, requireContext())
        viewModel.eventsFlow.observe(viewLifecycleOwner, ::updateUi)
    }

    private fun updateUi(model: StepsDrawingViewModel.Event) {
        when (model) {
            is SetUp -> {
                binding.buttonBack.setOnClickListener { viewModel.didOnClickButtonBack() }
                binding.buttonNext.setOnClickListener { viewModel.didOnClickButtonNext() }
            }
            is ShowImage -> {
                Utils.image(requireContext(), model.image, binding.imageView)
                binding.txtTitle.text = model.name
            }
            is ShowLoad -> binding.progressBar2.isVisible = model.isVisible
            is ShowNumberImages -> binding.txtNumberImages.text = getString(model.restId, model.currentNumber, model.numberImages)
            is ShowButtonBack -> binding.buttonBack.isVisible = model.isVisible
            is ShowButtonNext -> binding.buttonNext.isVisible = model.isVisible
            is InitialInterstitial -> {
                // add default ca-app-pub-3940256099942544/1033173712
                MobileAds.initialize(requireContext()) {}
                InterstitialAd.load(requireContext(),"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        mInterstitialAd = null
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        mInterstitialAd = interstitialAd
                    }
                })
                mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                        Log.d(ContentValues.TAG, "Ad was clicked.")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        Log.d(ContentValues.TAG, "Ad dismissed fullscreen content.")
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        // Called when ad fails to show.
                        Log.e(ContentValues.TAG, "Ad failed to show fullscreen content.")
                        mInterstitialAd = null
                    }

                    override fun onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d(ContentValues.TAG, "Ad recorded an impression.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(ContentValues.TAG, "Ad showed fullscreen content.")
                    }
                }
            }
            is ShowInterstitial -> {
                if (mInterstitialAd != null && model.isVisible) {
                    mInterstitialAd?.show(requireActivity())
                    viewModel.showedInterstitial()
                }
            }
            is ShowDifficulty -> {
                when(model.difficulty) {
                    1-> { binding.imgPencil1.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil))
                        binding.imgPencil2.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil_empty))
                        binding.imgPencil3.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil_empty))
                        binding.imgPencil4.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil_empty))
                        binding.imgPencil5.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil_empty))
                    }
                    2-> { binding.imgPencil1.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil))
                        binding.imgPencil2.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil))
                        binding.imgPencil3.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil_empty))
                        binding.imgPencil4.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil_empty))
                        binding.imgPencil5.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil_empty))
                    }
                    3-> { binding.imgPencil1.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil))
                        binding.imgPencil2.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil))
                        binding.imgPencil3.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil))
                        binding.imgPencil4.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil_empty))
                        binding.imgPencil5.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil_empty))
                    }
                    4-> { binding.imgPencil1.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil))
                        binding.imgPencil2.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil))
                        binding.imgPencil3.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil))
                        binding.imgPencil4.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil))
                        binding.imgPencil5.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil_empty))
                    }
                    5-> { binding.imgPencil1.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil))
                        binding.imgPencil2.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil))
                        binding.imgPencil3.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil))
                        binding.imgPencil4.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil))
                        binding.imgPencil5.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pencil))
                    }
                }

            }
        }
    }
}