package com.como.dibujar.personas.realistas.ui.view.main

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.como.dibujar.personas.realistas.databinding.ActivityMainBinding
import com.como.dibujar.personas.realistas.ui.base.BaseActivity
import com.como.dibujar.personas.realistas.ui.common.GOOGLE_PLAY
import com.como.dibujar.personas.realistas.ui.common.Utils
import com.como.dibujar.personas.realistas.ui.common.extension.observe
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.como.dibujar.personas.realistas.ui.view.main.MainViewModel.Event.*

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val adRequest = AdRequest.Builder().build()
    private var mInterstitialAd: InterstitialAd? = null


    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.initFlow()
        viewModel.eventsFlow.observe(this, ::updateUi)

    }

    private fun updateUi(model: MainViewModel.Event) {
        when (model) {
            is SetUp -> {
                MobileAds.initialize(this) {}
                binding.adViewSup.loadAd(adRequest)
                binding.adViewInf.loadAd(adRequest)
                binding.buttonShare.setOnClickListener { viewModel.didOnClickButtonShare() }
            }
            is ShowBanner -> {
                binding.adViewInf.isVisible = model.isVisible
                binding.adViewSup.isVisible = model.isVisible
            }
            is ShareApp -> Utils.shareApp(getString(model.ResInt, GOOGLE_PLAY), this)
        }
    }


}
