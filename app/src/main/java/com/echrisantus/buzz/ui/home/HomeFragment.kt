package com.echrisantus.buzz.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.recyclerview.widget.RecyclerView
import com.echrisantus.buzz.R
import com.echrisantus.buzz.adapter.*
import com.echrisantus.buzz.database.model.Movie
import com.echrisantus.buzz.database.model.TV
import com.echrisantus.buzz.databinding.FragmentHomeBinding
import com.echrisantus.buzz.databinding.TrendingBottomsheetBinding
import com.echrisantus.buzz.network.model.*
import com.echrisantus.buzz.ui.MainActivity
import com.echrisantus.buzz.util.Status
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.trending_bottomsheet.*
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }

    private lateinit var binding: FragmentHomeBinding

    private lateinit var trendingBottomsheetBinding: TrendingBottomsheetBinding
    private var bottomSheetDialog: BottomSheetDialog? = null

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var tvSeriesAdapter: TVAdapter

    var moviePageCount = 1
    var tvPageCount = 1

    private lateinit var listOfMovies: MutableList<MovieList>
    private lateinit var listOfTvSeries: MutableList<TVList>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner

        listOfMovies = mutableListOf()
        listOfTvSeries = mutableListOf()

        movieAdapter = MovieAdapter(object: MovieListener {
            override fun onClick(movie: MovieList) {
                findNavController().navigate(HomeFragmentDirections.homeToMovieDetails(movie.id))
            }
            override fun onLiked(movie: MovieList) {
                viewModel.saveFavouriteMovie(movie.id)
                Toast.makeText(activity, "Added to favourites", Toast.LENGTH_SHORT).show()
            }

            override fun onClickFav(movie: Movie) {}
            override fun onDelIconClick(movie: Movie) {}
        })

        tvSeriesAdapter = TVAdapter(object: TvListener {
            override fun onClick(tvList: TVList) {
                findNavController().navigate(HomeFragmentDirections.homeToTvDetails(tvList.id))
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
                ++moviePageCount
                viewModel.getMov(moviePageCount)
            }
        })

        binding.recyclerviewTv.apply {
            adapter = tvSeriesAdapter
        }

        tvSeriesAdapter.setOnBottomReachListener(object: OnBottomReachListener {
            override fun onBottomReached(position: Int) {
                ++tvPageCount
                viewModel.getTv(tvPageCount)
            }
        })

        mediaTypeSlider.setOnClickListener {
            viewModel.switchMediaTypes()

            listOfMovies.clear()
            listOfTvSeries.clear()
        }
        linearPopular.setOnClickListener {
            listOfMovies.clear()
            listOfTvSeries.clear()
            viewModel.setCategory(Category.POPULAR)
        }
        linearTopRated.setOnClickListener {
            listOfMovies.clear()
            listOfTvSeries.clear()
            viewModel.setCategory(Category.TOP_RATED)
        }
        linearUpcoming.setOnClickListener {
            listOfMovies.clear()
            viewModel.setCategory(Category.UPCOMING)
        }

        fabFav.setOnClickListener { findNavController().navigate(HomeFragmentDirections.homeToFavourites()) }

        trendingCategory.setOnClickListener {
            if (!bottomSheetDialog!!.isShowing) {
                bottomSheetDialog?.show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as AppCompatActivity).finish()
            }
        })

        handleViewModels()
        handleBottomSheet()
    }

    private fun handleBottomSheet() {
        trendingBottomsheetBinding = DataBindingUtil.inflate(layoutInflater, R.layout.trending_bottomsheet, null, false)
        val bottomSheetView = trendingBottomsheetBinding.root
        bottomSheetDialog = BottomSheetDialog(activity as AppCompatActivity)
        bottomSheetDialog?.setContentView(bottomSheetView)

        var mediaType = MediaType.MOVIE.value
        var timeWindow = TimeWindow.DAY.value

        trendingBottomsheetBinding.mediaTypeSwitch.setOnCheckedChangeListener { _, isChecked ->
            run {
                mediaType = if (isChecked) MediaType.MOVIE.value else MediaType.TV.value
                trendingBottomsheetBinding.mediaTypeSwitch.text = mediaType
            }
        }

        trendingBottomsheetBinding.timeWindowSwitch.setOnCheckedChangeListener { _, isChecked ->
            run {
                timeWindow = if (isChecked) TimeWindow.DAY.value else TimeWindow.WEEK.value
                trendingBottomsheetBinding.timeWindowSwitch.text = timeWindow
            }
        }

        trendingBottomsheetBinding.search.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.homeToSearch(timeWindow, mediaType))
            bottomSheetDialog?.dismiss()

        }
    }

    private fun handleViewModels() {
        with(viewModel) {
            handleMovieRetrieval(1)

            handleTvRetrieval(1)

            movieList.observe(viewLifecycleOwner, Observer {
                it?.let {
                    listOfMovies.addAll(it)
                    movieAdapter.addItemAndSubmit(listOfMovies)
                }
            })
            tvList.observe(viewLifecycleOwner, Observer {
                it?.let {
                    listOfTvSeries.addAll(it)
                    tvSeriesAdapter.addItemAndSubmit(listOfTvSeries)
                }
            })
        }
    }

    private fun handleMovieRetrieval(page: Int) {
        viewModel.getMovies(page).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerviewMovie.visibility = if(StringUtils.equalsIgnoreCase(viewModel._mediaType.value, MediaType.MOVIE.value)) View.VISIBLE else View.GONE
                        progressBar.visibility = View.GONE
                        listOfMovies.addAll(resource.data!!.results)
                        movieAdapter.addItemAndSubmit(listOfMovies)
                    }
                    Status.ERROR -> {
                        recyclerviewMovie.visibility = if(StringUtils.equalsIgnoreCase(viewModel._mediaType.value, MediaType.MOVIE.value)) View.VISIBLE else View.GONE
                        progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerviewMovie.visibility = View.GONE
                    }
                }
            }

        })
    }

    private fun handleTvRetrieval(page: Int) {
        viewModel.getTvSeries(page).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerviewTv.visibility =if(StringUtils.equalsIgnoreCase(viewModel._mediaType.value, MediaType.TV.value)) View.VISIBLE else View.GONE
                        progressBar.visibility = View.GONE
                        listOfTvSeries.addAll(resource.data!!.results)
                        tvSeriesAdapter.addItemAndSubmit(listOfTvSeries)
                    }
                    Status.ERROR -> {
                        recyclerviewTv.visibility = if(StringUtils.equalsIgnoreCase(viewModel._mediaType.value, MediaType.TV.value)) View.VISIBLE else View.GONE
                        progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerviewTv.visibility = View.GONE
                    }
                }
            }
        })
    }
}
