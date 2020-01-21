package com.mok.moviespaginatedlist.genres.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mok.moviespaginatedlist.genres.models.Genre
import io.reactivex.Completable


@Dao
interface GenreDao {


    @Query("SELECT * FROM genre Where genre_id == :genreId ")
    fun getGenreById(genreId: Int): LiveData<Genre>


    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllGenres(list: List<Genre>): Completable

    @Query("DELETE FROM genre")
    fun deleteAllGenres(): Completable

}