package ru.itis.hw5.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.itis.hw5.data.database.AppDatabase.Companion.DATABASE_VERSION
import ru.itis.hw5.data.database.dao.MovieDao
import ru.itis.hw5.data.database.dao.UserDao
import ru.itis.hw5.data.database.entities.Movie
import ru.itis.hw5.data.database.entities.User

@Database(
    entities = [User::class, Movie::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun movieDao(): MovieDao

    companion object {
        private const val DATABASE_VERSION = 1
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movie_vault.db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}