package com.echrisantus.buzz.ui.tv


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.echrisantus.buzz.R
import com.echrisantus.buzz.adapter.GuestStarAdapter
import com.echrisantus.buzz.adapter.ItemListener
import com.echrisantus.buzz.databinding.EpisodeBottomsheetBinding
import com.echrisantus.buzz.databinding.FragmentTvSeriesMoreDetailsBinding
import com.echrisantus.buzz.network.model.TVEpisode
import com.echrisantus.buzz.util.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_tv_series_more_details.*
import kotlinx.android.synthetic.main.fragment_tv_series_more_details.view.overview
import kotlinx.android.synthetic.main.tvseries_view_more_dialog.view.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class TvSeriesMoreDetailsFragment :  DaggerFragment() {

    private lateinit var binding: FragmentTvSeriesMoreDetailsBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<TVSeriesDetailViewModel> { viewModelFactory }
    private lateinit var argument: TvSeriesMoreDetailsFragmentArgs

    private lateinit var guestStarAdapter: GuestStarAdapter

    private lateinit var episodeBottomsheetBinding: EpisodeBottomsheetBinding
    private var bottomSheetDialog: BottomSheetDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tv_series_more_details, container, false)
        binding = FragmentTvSeriesMoreDetailsBinding.bind(view).apply {
            viewmodel = viewModel
        }
        argument = TvSeriesMoreDetailsFragmentArgs.fromBundle(arguments)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner

        guestStarAdapter = GuestStarAdapter(ItemListener {
            viewModel.showStarBottomSheet(it)
        })

        binding.recyclerviewCast.apply {
            adapter = guestStarAdapter
            layoutManager = LinearLayoutManager(activity!!.applicationContext, LinearLayoutManager.HORIZONTAL, false)
        }

        menu.setOnClickListener {
            if (!bottomSheetDialog!!.isShowing) {
                bottomSheetDialog?.show()
            }
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

        handleViewModels()

        handleEpisodeBottomSheet()
    }

    private fun handleViewModels() {
        with(viewModel) {
            getTvMoreDetails(argument.id, _season.value!!.toInt(), _episode.value!!.toInt()).observe(viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            relativeLayout.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            resource.data?.let { response -> setTvEpisodeData(response.body()!!) }
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

            guestStars.observe(viewLifecycleOwner, Observer {
                it?.let {
                    guestStarAdapter.addItemAndSubmit(it)
                }
            })
            episodeArray.observe(viewLifecycleOwner, Observer {
                it?.let {
                    episodeBottomsheetBinding.episodeAdapter = ArrayAdapter<String>(activity!!.applicationContext, R.layout.spinner_item, viewModel._episodeArray.value!!).apply {
                        setDropDownViewResource(R.layout.spinner_item)
                    }
                }
            })

            season.observe(viewLifecycleOwner, Observer {
                it?.let {
                    viewModel.getNumberOfEpisodes(argument.id, it.toInt())
                }
            })
        }
    }

    private fun handleEpisodeBottomSheet() {
        episodeBottomsheetBinding = DataBindingUtil.inflate(layoutInflater, R.layout.episode_bottomsheet, null, false)
        episodeBottomsheetBinding.viewmodel = viewModel

        val bottomSheetView = episodeBottomsheetBinding.root
        bottomSheetDialog = BottomSheetDialog(activity as AppCompatActivity)
        bottomSheetDialog?.setContentView(bottomSheetView)

        episodeBottomsheetBinding.seasonAdapter = ArrayAdapter<String>(activity!!.applicationContext, R.layout.spinner_item, viewModel.getNumberOfSeasonsArray(argument.noOfSeasons)).apply {
            setDropDownViewResource(R.layout.spinner_item)
        }

        episodeBottomsheetBinding.episodeAdapter = ArrayAdapter<String>(activity!!.applicationContext, R.layout.spinner_item, viewModel._episodeArray.value!!).apply {
            setDropDownViewResource(R.layout.spinner_item)
        }

        episodeBottomsheetBinding.viewButton.setOnClickListener {
            viewModel.getTvMoreDetails(argument.id, viewModel._season.value!!.toInt(), viewModel._episode.value!!.toInt()).observe(viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            relativeLayout.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                            resource.data?.let { response -> setTvEpisodeData(response.body()!!) }
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

            bottomSheetDialog?.dismiss()
        }
    }

    private fun setTvEpisodeData(tvEpisode: TVEpisode) {
        viewModel._tvEpisode.value = tvEpisode
        viewModel._guestStars.value = tvEpisode.guestStar
    }

    private fun displayMoreDialog() {
        val dialogBuilder = AlertDialog.Builder(activity as AppCompatActivity)
        val view = layoutInflater.inflate(R.layout.tvseries_view_more_dialog, null)
        dialogBuilder.setView(view)

        view.overview.text = viewModel.tvEpisode.value!!.overview
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        view.closeButton.setOnClickListener { alertDialog.dismiss() }

        view.viewDetails.visibility = View.GONE
        view.horline.visibility = View.GONE
    }

}
