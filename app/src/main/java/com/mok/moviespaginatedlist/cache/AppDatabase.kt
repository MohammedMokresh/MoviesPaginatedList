package com.mok.moviespaginatedlist.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mok.moviespaginatedlist.genres.Genre
import com.mok.moviespaginatedlist.languages.LanguagesResponseBody
import com.mok.moviespaginatedlist.models.Result
import com.mok.moviespaginatedlist.utils.Converters

@Database(
    entities = [Result::class, Genre::class, LanguagesResponseBody::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDAO(): MovieDao

    abstract fun languageDao(): LanguageDao

    abstract fun genreDao(): GenreDao


    companion object {

        private const val DATABASE_NAME = "movies_DB"
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}