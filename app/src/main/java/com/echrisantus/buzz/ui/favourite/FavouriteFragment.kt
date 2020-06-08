package com.echrisantus.buzz.ui.favourite


import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.echrisantus.buzz.R
import com.echrisantus.buzz.adapter.*
import com.echrisantus.buzz.database.model.Movie
import com.echrisantus.buzz.database.model.TV
import com.echrisantus.buzz.databinding.FragmentFavouriteBinding
import com.echrisantus.buzz.network.model.MediaType
import com.echrisantus.buzz.network.model.MovieList
import com.echrisantus.buzz.network.model.TVList
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.fragment_favourite.mediaTypeSwitch
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : DaggerFragment() {

    private lateinit var binding: FragmentFavouriteBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<FavouriteViewModel> { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
        binding = FragmentFavouriteBinding.bind(view).apply {
            viewmodel = viewModel
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner

        val movieAdapter = FavMovieAdapter(object: MovieListener{
            override fun onClick(movie: MovieList) {}
            override fun onLiked(movie: MovieList) {}

            override fun onClickFav(movie: Movie) {
                findNavController().navigate(FavouriteFragmentDirections.favouriteToDetail(movie.id, MediaType.MOVIE.value))
            }
            override fun onDelIconClick(movie: Movie) {
                viewModel.deleteFavMovie(movie.id)
            }
        })

        val tvSeriesAdapter = FavTVAdapter(object: TvListener{
            override fun onClick(tvList: TVList) {}
            override fun onLiked(tvList: TVList) {}

            override fun onClickFav(tv: TV) {
                findNavController().navigate(FavouriteFragmentDirections.favouriteToDetail(tv.id, MediaType.TV.value))
            }
            override fun onDelIconClick(tv: TV) {
                viewModel.deleteFavSeries(tv.id)
            }
        })

        binding.recyclerviewMovie.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        }
        binding.recyclerviewTv.apply {
            adapter = tvSeriesAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        }

        mediaTypeSwitch.setOnClickListener {
            viewModel.switchMediaTypes()
        }

        deleteIcon.setOnClickListener {
            viewModel.clearFavourites()
        }

        backIcon.setOnClickListener {
            findNavController().navigate(FavouriteFragmentDirections.favouriteToHome())
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(FavouriteFragmentDirections.favouriteToHome())
            }
        })

        with(viewModel) {
            movieList.observe(viewLifecycleOwner, Observer {
                it?.let {
                    movieAdapter.addItemAndSubmit(it)
                }
            })
            tvList.observe(viewLifecycleOwner, Observer {
                it?.let {
                    tvSeriesAdapter.addItemAndSubmit(it)
                }
            })
            deleted.observe(viewLifecycleOwner, Observer {
                if (it == true) {
                    Toast.makeText(activity, "Favourites cleared!", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
