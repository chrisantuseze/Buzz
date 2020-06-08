package com.echrisantus.buzz.ui.favourite


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.echrisantus.buzz.databinding.FragmentFavouriteDetailBinding
import com.echrisantus.buzz.ui.tv.TVSeriesDetailFragmentDirections
import com.echrisantus.buzz.util.Status
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_favourite_detail.*
import kotlinx.android.synthetic.main.movie_view_more_dialog.view.*
import kotlinx.android.synthetic.main.movie_view_more_dialog.view.closeButton
import kotlinx.android.synthetic.main.movie_view_more_dialog.view.overview
import kotlinx.android.synthetic.main.tvseries_view_more_dialog.view.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class FavouriteDetailFragment : DaggerFragment() {

    private lateinit var binding: FragmentFavouriteDetailBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<FavouriteViewModel> { viewModelFactory }
    private lateinit var argument: FavouriteDetailFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favourite_detail, container, false)
        binding = FragmentFavouriteDetailBinding.bind(view).apply {
            viewmodel = viewModel
        }
        argument = FavouriteDetailFragmentArgs.fromBundle(arguments)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner


        with(viewModel) {
            setMediaType(argument.mediaType)

            getMovie(argument.id).observe(viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            movieRelativeLayout.visibility = View.VISIBLE
                            movieProgressBar.visibility = View.GONE
                            resource.data?.let { response -> _movie.value = response }
                        }
                        Status.ERROR -> {
                            movieRelativeLayout.visibility = View.VISIBLE
                            movieProgressBar.visibility = View.GONE
                            Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING -> {
                            movieProgressBar.visibility = View.VISIBLE
                            movieRelativeLayout.visibility = View.GONE
                        }
                    }
                }
            })

            getTvSeries(argument.id).observe(viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            tvRelativeLayout.visibility = View.VISIBLE
                            tvProgressBar.visibility = View.GONE
                            resource.data?.let { response -> _tv.value = response }
                        }
                        Status.ERROR -> {
                            tvRelativeLayout.visibility = View.VISIBLE
                            tvProgressBar.visibility = View.GONE
                            Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING -> {
                            tvProgressBar.visibility = View.VISIBLE
                            tvRelativeLayout.visibility = View.GONE
                        }
                    }
                }
            })
        }

        movieViewMore.setOnClickListener { displayMoreMovieDialog() }

        tvViewMore.setOnClickListener { displayMoreTvDialog() }



        movieBackIcon.setOnClickListener {
            findNavController().navigate(FavouriteDetailFragmentDirections.favouriteDetailToFavourite())
        }

        tvBackIcon.setOnClickListener {
            findNavController().navigate(FavouriteDetailFragmentDirections.favouriteDetailToFavourite())
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(FavouriteDetailFragmentDirections.favouriteDetailToFavourite())
            }
        })
    }

    private fun displayMoreMovieDialog() {
        val dialogBuilder = AlertDialog.Builder(activity as AppCompatActivity)
        val view = layoutInflater.inflate(R.layout.movie_view_more_dialog, null)
        dialogBuilder.setView(view)

        view.overview.text = viewModel._movie.value!!.overview
        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        view.closeButton.setOnClickListener { alertDialog.dismiss() }
    }

    private fun displayMoreTvDialog() {
        val dialogBuilder = AlertDialog.Builder(activity as AppCompatActivity)
        val view = layoutInflater.inflate(R.layout.tvseries_view_more_dialog, null)
        dialogBuilder.setView(view)

        view.overview.text = viewModel._tv.value!!.overview
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        view.closeButton.setOnClickListener { alertDialog.dismiss() }

        view.viewDetails.setOnClickListener {
            findNavController().navigate(FavouriteDetailFragmentDirections.favouriteDetailToTvMoreDetails(argument.id, viewModel._tv.value!!.numberOfSeasons))
            alertDialog.dismiss()
        }
    }

}
