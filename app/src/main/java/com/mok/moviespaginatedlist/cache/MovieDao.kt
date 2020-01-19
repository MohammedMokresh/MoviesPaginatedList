package com.mok.moviespaginatedlist.cache

import androidx.paging.DataSource
import androidx.room.*
import com.mok.moviespaginatedlist.genres.Genre
import com.mok.moviespaginatedlist.languages.LanguagesResponseBody
import com.mok.moviespaginatedlist.models.Result
import io.reactivex.Completable
import io.reactivex.Single


@Dao
interface MovieDao {


    @Query("SELECT * FROM movie")
    fun movies(): Single<List<Result>>


    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMovies(list: List<Result>): Completable

    @Query("SELECT * FROM movie ORDER BY release_date DESC")
    fun getAllPaged(): DataSource.Factory<Int, Result>

    @Query("DELETE FROM movie")
    fun deleteAllMovies(): Completable


    @Query("DELETE FROM movie WHERE id NOT IN (SELECT id from movie ORDER BY release_date DESC limit 10)")
    fun deleteAllExceptfirstTen()



}