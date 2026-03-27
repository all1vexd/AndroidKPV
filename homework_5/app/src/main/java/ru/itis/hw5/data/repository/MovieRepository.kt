package ru.itis.hw5.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import ru.itis.hw5.constants.StatusConstant
import ru.itis.hw5.data.database.AppDatabase
import ru.itis.hw5.data.database.entities.Movie
import ru.itis.hw5.data.database.entities.User
import java.lang.Exception

class MovieRepository (
    private val database: AppDatabase
) {
    private val movieDao = database.movieDao()

    suspend fun addMovie(movie: Movie): Boolean {
        return try {
            movieDao.insert(movie)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateMovie(movie: Movie): Boolean {
        return try {
            movieDao.update(movie)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteMovie(movie: Movie): Boolean {
        return try {
            movieDao.delete(movie)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteMovieById(movieId: Int, userId: Int): Boolean {
        return try {
            movieDao.deleteById(movieId, userId)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getMovieById(movieId: Int, userId: Int): Movie? {
        return movieDao.getMovieById(movieId, userId)
    }

    fun getMoviesByUser(userId: Int): Flow<List<Movie>> {
        return movieDao.getMoviesByUser(userId)
    }

    fun getMoviesSortedByRating(userId: Int): Flow<List<Movie>> {
        return movieDao.getMoviesSortedByRating(userId)
    }

    fun getMoviesSortedByDate(userId: Int, newestFirst: Boolean = true): Flow<List<Movie>> {
        return if (newestFirst) {
            movieDao.getMoviesSortedByDateDesc(userId)
        } else {
            movieDao.getMoviesSortedByDateAsc(userId)
        }
    }

    fun getMoviesSortedByTitle(userId: Int): Flow<List<Movie>> {
        return movieDao.getMoviesSortedByTitle(userId)
    }

    fun getMoviesByStatus(userId: Int, status: String): Flow<List<Movie>> {
        return movieDao.getMoviesByStatus(userId, status)
    }

    fun getFavoriteMovies(userId: Int): Flow<List<Movie>> {
        return movieDao.getFavoriteMovies(userId)
    }

    fun searchMovies(userId: Int, query: String): Flow<List<Movie>> {
        return if (query.isBlank()) {
            getMoviesByUser(userId)
        } else {
            movieDao.searchMovies(userId, query)
        }
    }

    suspend fun getMovieCount(userId: Int): Int {
        return movieDao.getMovieCount(userId)
    }

    suspend fun getAverageRating(userId: Int): Float? {
        return movieDao.getAverageRating(userId)
    }

    suspend fun getStatusStatistics(userId: Int): Map<String, Int> {
        val movies = movieDao.getMoviesByUser(userId).first()
        return movies.groupingBy { it.status }.eachCount()
    }

    suspend fun toggleFavorite(movieId: Int, userId: Int): Boolean {
        return try {
            val movie = getMovieById(movieId, userId)
            movie?.let {
                it.isFavorite = !it.isFavorite
                updateMovie(it)
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateMovieRating(movieId: Int, userId: Int, rating: Float?): Boolean {
        return try {
            val movie = getMovieById(movieId, userId)
            movie?.let {
                it.rating = rating
                updateMovie(it)
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateMovieStatus(movieId: Int, userId: Int, status: String): Boolean {
        return try {
            val movie = getMovieById(movieId, userId)
            movie?.let {
                it.status = status
                updateMovie(it)
            } ?: false
        } catch (e: Exception) {
            false
        }
    }
}