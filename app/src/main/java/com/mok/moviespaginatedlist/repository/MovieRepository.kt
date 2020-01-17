package com.mok.moviespaginatedlist.repository

import androidx.paging.toLiveData
import com.mok.moviespaginatedlist.MoviesListService
import com.mok.moviespaginatedlist.cache.MovieDao
import com.mok.moviespaginatedlist.models.Result
import com.mok.moviespaginatedlist.utils.liveData
import com.mok.moviespaginatedlist.utils.paginationUtils.GenericBoundaryCallback
import com.mok.moviespaginatedlist.utils.paginationUtils.Listing
import io.reactivex.Completable
import io.reactivex.Single

interface MovieRepository {


    companion object {
        const val SIZE_PAGE = 30
    }


    fun getListable(): Listing<Result>

    open class MoviesRepositoryImpl(
        private val service: MoviesListService,
        private val dao: MovieDao
    ) : MovieRepository {


        override fun getListable(): Listing<Result> {
            return object :
                Listing<Result> {

                /** Create the boundary callback **/
                val bc: GenericBoundaryCallback<Result> by lazy {
                    GenericBoundaryCallback(
                        { dao.deleteAll() },
                        { character(it, SIZE_PAGE) },
                        { insertCharacters(it) },
                        SIZE_PAGE
                    )
                }

                override fun getDataSource() =
                    dao.getAllPaged().map { it }.toLiveData(
                        pageSize = SIZE_PAGE,
                        boundaryCallback = bc
                    )


                override fun getBoundaryCallback() = liveData(bc)

            }
        }

        fun insertCharacters(list: List<Result>): Completable {
            return dao.insertAll(list.map { it })
        }

        fun character(offset: Int, pageSize: Int): Single<List<Result>> =
            service.movies(offset, pageSize)
                .map { it }

    }

}