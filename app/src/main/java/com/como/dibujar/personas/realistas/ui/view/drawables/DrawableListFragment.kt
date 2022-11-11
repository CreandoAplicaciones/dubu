package com.como.dibujar.personas.realistas.ui.view.drawables

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.como.dibujar.personas.realistas.databinding.FragmentDrawableListBinding
import com.como.dibujar.personas.realistas.ui.base.BaseFragment
import com.como.dibujar.personas.realistas.ui.common.GOOGLE_PLAY
import com.como.dibujar.personas.realistas.ui.common.Utils
import com.como.dibujar.personas.realistas.ui.common.extension.observe
import com.como.dibujar.personas.realistas.ui.view.doalograte.DialogRateApp
import com.como.dibujar.personas.realistas.ui.view.drawables.DrawableListViewModel.Event.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.como.dibujar.personas.realistas.R


class DrawableListFragment : BaseFragment(), DialogRateApp.OnClickListener {

    private val viewModel: DrawableListViewModel by viewModels()
    private lateinit var binding: FragmentDrawableListBinding
    private val adRequest = AdRequest.Builder().build()
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDrawableListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.init(viewModel)
        viewModel.initFlow(requireContext())
        viewModel.eventsFlow.observe(viewLifecycleOwner, ::updateUi)
    }

    private fun updateUi(model: DrawableListViewModel.Event) {
        when (model) {
            is SetUp -> {
                binding.iVBody.setOnClickListener { viewModel.didClickBodyButton() }
                binding.iVFace.setOnClickListener { viewModel.didClickFaceButton() }
                binding.iVHand.setOnClickListener { viewModel.didClickHandButton() }
            }
            is ShowLoad -> binding.progressBar.isVisible = model.isVisibility
            is ShowListFace -> {
                binding.rVImages.adapter = DrawableAdapter(model.imagesData, viewModel::didSelectedImage)
            }
            is ShowListBody -> {
                binding.rVImages.adapter = DrawableAdapter(model.imagesData, viewModel::didSelectedImage)
            }
            is ShowListHand -> {
                binding.rVImages.adapter = DrawableAdapter(model.imagesData, viewModel::didSelectedImage)
            }
            is GoToStepsDrawing -> findNavController().navigate(DrawableListFragmentDirections.actionDrawableListFragmentToStepsDrawingFragment(model.collection, model.id))
            is SelectedBody -> {
                binding.iVHand.setBackgroundColor(binding.iVHand.context.resources.getColor(model.white))
                binding.iVFace.setBackgroundColor(binding.iVHand.context.resources.getColor(model.white))
                binding.iVBody.background = resources.getDrawable(model.background)
            }
            is SelectedFace -> {

                binding.iVBody.setBackgroundColor(binding.iVHand.context.resources.getColor(model.white))
                binding.iVHand.setBackgroundColor(binding.iVHand.context.resources.getColor(model.white))
                binding.iVFace.background = resources.getDrawable(model.background)
            }
            is SelectedHand -> {
                binding.iVBody.setBackgroundColor(binding.iVHand.context.resources.getColor(model.white))
                binding.iVFace.setBackgroundColor(binding.iVHand.context.resources.getColor(model.white))
                binding.iVHand.background = resources.getDrawable(model.background)
            }
            is InitialInterstitial -> {
                // add default ca-app-pub-3940256099942544/1033173712
                // add mio ca-app-pub-4849545913451935~8657828105
                MobileAds.initialize(requireContext()) {}
                InterstitialAd.load(requireContext(),"ca-app-pub-4849545913451935~8657828105", adRequest, object : InterstitialAdLoadCallback() {
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
            is ShowDialogRate -> {
                if(model.isVisible) {
                    val rate = DialogRateApp(this)
                    rate.show(parentFragmentManager, null)
                }
            }
        }
    }

    override fun goToGooglePlay(start: Int) {
        if (start == 5 || start == 4) {
            val uri = Uri.parse(GOOGLE_PLAY)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } else {
            Utils.toast(requireContext(), R.string.dialog_rate_thank_you)
        }
    }
}