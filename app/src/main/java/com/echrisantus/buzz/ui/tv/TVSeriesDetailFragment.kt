package com.echrisantus.buzz.ui.tv


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.echrisantus.buzz.R
import com.echrisantus.buzz.databinding.FragmentTvdetailSeriesBinding
import com.echrisantus.buzz.network.model.TVData
import com.echrisantus.buzz.util.Status
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_tvdetail_series.*
import kotlinx.android.synthetic.main.tvseries_view_more_dialog.view.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class TVSeriesDetailFragment : DaggerFragment() {

    private lateinit var binding: FragmentTvdetailSeriesBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<TVSeriesDetailViewModel> { viewModelFactory }
    private lateinit var argument: TVSeriesDetailFragmentArgs


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tvdetail_series, container, false)
        binding = FragmentTvdetailSeriesBinding.bind(view).apply {
            viewmodel = viewModel
        }
        argument = TVSeriesDetailFragmentArgs.fromBundle(arguments)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner

        with(viewModel) {

            getTvSeries(argument.id).observe(viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            relativeLayout.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            resource.data?.let { response -> retrieveTvSeries(response.body()!!) }
                        }
                        Status.ERROR -> {
                            relativeLayout.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING -> {
                            progressBar.visibility = View.VISIBLE
                            relativeLayout.visibility = View.GONE
                        }
                    }
                }
            })
        }

        likeIcon.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(activity!!.applicationContext, R.anim.fade)
            viewModel.saveFavouriteTvSeries().observe(viewLifecycleOwner, Observer {
                it?.let {resource ->
                    when(resource.status) {
                        Status.SUCCESS -> {
                            progressBarFav.visibility = View.GONE
                            backdrop.invalidate()
                            Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show()
                        }
                        Status.ERROR -> {
                            progressBarFav.visibility = View.GONE
                            Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show()
                        }
                        Status.LOADING -> {
                            progressBarFav.visibility = View.VISIBLE
                            backdrop.startAnimation(animation)
                        }
                    }
                }
            })
        }

        viewMore.setOnClickListener { displayMoreDialog() }

        backIcon.setOnClickListener {
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })
    }

    private fun retrieveTvSeries(tv: TVData) {
        viewModel._tv.value = tv
    }

    private fun displayMoreDialog() {
        val dialogBuilder = AlertDialog.Builder(activity as AppCompatActivity)
        val view = layoutInflater.inflate(R.layout.tvseries_view_more_dialog, null)
        dialogBuilder.setView(view)

        view.overview.text = viewModel._tv.value!!.overview
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        view.closeButton.setOnClickListener { alertDialog.dismiss() }

        view.viewDetails.setOnClickListener {
            findNavController().navigate(TVSeriesDetailFragmentDirections.tvDetailsToTvMoreDetails(argument.id, viewModel._tv.value!!.numberOfSeasons))
            alertDialog.dismiss()
        }
    }

}
