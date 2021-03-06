package com.mok.moviespaginatedlist.moviesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mok.moviespaginatedlist.moviesList.models.Result
import com.mok.moviespaginatedlist.moviesList.data.MovieRepository
import com.mok.moviespaginatedlist.utils.liveData
import com.mok.moviespaginatedlist.utils.paginationUtils.GenericBoundaryCallback
import com.mok.moviespaginatedlist.utils.paginationUtils.Listing

class MoviesViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {


    private val listing: LiveData<Listing<Result>> by lazy {
        liveData(movieRepository.getListable())
    }

    private val boundaryCallback = Transformations.switchMap(listing) { it.getBoundaryCallback() }
    val dataSource = Transformations.switchMap(listing) { it.getDataSource() }
    val networkState = Transformations.switchMap(listing) { it.getNetworkState() }


    fun refreshPage(): LiveData<GenericBoundaryCallback<Result>> {
        return Transformations.switchMap(listing) { it.getBoundaryCallback() }
    }

    fun deleteAllExceptfirstTen() {
        movieRepository.deleteAllExceptfirstTen()
    }

    fun getMovieById(movieId: Int): LiveData<Result> {
        return movieRepository.getMovieById(movieId)
    }

    /**
     * Retry all failed petitions boundary callback
     */
    fun retry() {
        boundaryCallback.value?.retryPetitions()
    }

    /**
     * Cleared all references and petitions boundary callback
     */
    override fun onCleared() {
        boundaryCallback.value?.cleared()
    }
}