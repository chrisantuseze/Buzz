package com.echrisantus.buzz.ui.search


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController

import com.echrisantus.buzz.R
import com.echrisantus.buzz.adapter.*
import com.echrisantus.buzz.database.model.Movie
import com.echrisantus.buzz.database.model.TV
import com.echrisantus.buzz.databinding.FragmentSearchBinding
import com.echrisantus.buzz.network.model.*
import com.echrisantus.buzz.ui.home.HomeFragmentDirections
import com.echrisantus.buzz.util.Status
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_search.*
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : DaggerFragment() {

    private lateinit var binding: FragmentSearchBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SearchViewModel> { viewModelFactory }
    private lateinit var argument: SearchFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        binding = FragmentSearchBinding.bind(view).apply {
            viewmodel = viewModel
        }
        argument = SearchFragmentArgs.fromBundle(arguments)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner

        val movieAdapter = MovieAdapter(object: MovieListener {
            override fun onClick(movie: MovieList) {
                val bundle = bundleOf("id" to movie.id)
//                NavHostFragment.findNavController(this@SearchFragment).navigate(R.id.action_global_movieDetailFragment, bundle)
                findNavController().navigate(SearchFragmentDirections.searchToMovieDetails(movie.id))
            }
            override fun onLiked(movie: MovieList) {
                viewModel.saveFavouriteMovie(movie.id)
                Toast.makeText(activity, "Added to favourites", Toast.LENGTH_SHORT).show()
            }

            override fun onClickFav(movie: Movie) {}
            override fun onDelIconClick(movie: Movie) {}
        })

        val tvSeriesAdapter = TVAdapter(object: TvListener {
            override fun onClick(tvList: TVList) {
                val bundle = bundleOf("id" to tvList.id)
//                NavHostFragment.findNavController(this@SearchFragment).navigate(R.id.action_global_tvDetailFragment, bundle)
                findNavController().navigate(SearchFragmentDirections.searchToTVDetails(tvList.id))
            }
            override fun onLiked(tvList: TVList) {
                viewModel.saveFavouriteTvSeries(tvList.id)
                Toast.makeText(activity, "Added to favourites", Toast.LENGTH_SHORT).show()
            }

            override fun onClickFav(tv: TV) {}
            override fun onDelIconClick(tv: TV) {}
        })

        binding.recyclerviewMovie.apply {
            adapter = movieAdapter
        }

        movieAdapter.setOnBottomReachListener(object: OnBottomReachListener {
            override fun onBottomReached(position: Int) {
            }
        })

        binding.recyclerviewTv.apply {
            adapter = tvSeriesAdapter
        }

        tvSeriesAdapter.setOnBottomReachListener(object: OnBottomReachListener {
            override fun onBottomReached(position: Int) {
            }
        })

        with(viewModel) {
            _trendingMediaType.value = "Trending ${argument.mediaType}"
            mediaType = argument.mediaType
            timeWindow = argument.timeWindow

            getTrending().observe(viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            if(StringUtils.equalsIgnoreCase(argument.mediaType, MediaType.MOVIE.value)) {
                                recyclerviewMovie.visibility = View.VISIBLE
                                recyclerviewTv.visibility = View.GONE
                                val data = resource.data as List<MovieList>
                                movieAdapter.addItemAndSubmit(data)
                            } else {
                                recyclerviewMovie.visibility = View.GONE
                                recyclerviewTv.visibility = View.VISIBLE
                                val data = resource.data as List<TVList>
                                tvSeriesAdapter.addItemAndSubmit(data)
                            }
                            progressBar.visibility = View.GONE
                        }

                        Status.ERROR -> {
                            if(StringUtils.equalsIgnoreCase(argument.mediaType, MediaType.MOVIE.value)) {
                                recyclerviewMovie.visibility =  View.VISIBLE
                                recyclerviewTv.visibility = View.GONE
                            } else {
                                recyclerviewMovie.visibility = View.GONE
                                recyclerviewTv.visibility = View.VISIBLE
                            }
                            progressBar.visibility = View.GONE
                            Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                        }

                        Status.LOADING -> {
                            progressBar.visibility = View.VISIBLE
                            recyclerviewMovie.visibility = View.GONE
                            recyclerviewTv.visibility = View.GONE
                        }
                    }
                }
            })
        }

        backIcon.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.searchToHome())
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(SearchFragmentDirections.searchToHome())
            }
        })
    }
}
