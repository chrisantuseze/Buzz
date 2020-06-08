package com.echrisantus.buzz.ui.splash


import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.echrisantus.buzz.R
import com.echrisantus.buzz.database.AppSharedPref
import com.echrisantus.buzz.databinding.FragmentHomeBinding
import com.echrisantus.buzz.databinding.FragmentSplashScreenBinding
import com.echrisantus.buzz.databinding.TrendingBottomsheetBinding
import com.echrisantus.buzz.ui.home.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SplashScreen : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SplashViewModel> { viewModelFactory }

    private lateinit var binding: FragmentSplashScreenBinding

    private lateinit var appSharedPref: AppSharedPref

    private var TIMEOUT = 3000L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner

        val ctx = activity as AppCompatActivity
        appSharedPref = AppSharedPref(ctx)

        if (findNavController().currentDestination?.id != R.id.splashScreen) {
            return
        }

        Handler().postDelayed({
            appSharedPref.setLogin(true)
            findNavController().navigate(R.id.homeFragment)
        }, TIMEOUT)
    }
}
