package ru.itis.hw5.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.itis.hw5.data.database.entities.Movie

@Dao
interface MovieDao {

    @Insert
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("DELETE FROM movies WHERE id = :movieId AND user_id = :userId")
    suspend fun deleteById(movieId: Int, userId: Int)


    // получаем все фильмы пользователя
    @Query("SELECT * FROM movies WHERE user_id = :userId")
    fun getMoviesByUser(userId: Int): Flow<List<Movie>>

    // Получить один фильм
    @Query("SELECT * FROM movies WHERE id = :movieId AND user_id = :userId")
    suspend fun getMovieById(movieId: Int, userId: Int): Movie?


    // По рейтингу
    @Query("""
        SELECT * FROM movies 
        WHERE user_id = :userId 
        ORDER BY 
            CASE WHEN rating IS NULL THEN 1 ELSE 0 END,
            rating DESC
    """)
    fun getMoviesSortedByRating(userId: Int): Flow<List<Movie>>

    // По дате добавления
    @Query("SELECT * FROM movies WHERE user_id = :userId ORDER BY added_date DESC")
    fun getMoviesSortedByDateDesc(userId: Int): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE user_id = :userId ORDER BY added_date ASC")
    fun getMoviesSortedByDateAsc(userId: Int): Flow<List<Movie>>

    // По названию
    @Query("SELECT * FROM movies WHERE user_id = :userId ORDER BY title COLLATE NOCASE ASC")
    fun getMoviesSortedByTitle(userId: Int): Flow<List<Movie>>


    // По статусу
    @Query("SELECT * FROM movies WHERE user_id = :userId AND status = :status")
    fun getMoviesByStatus(userId: Int, status: String): Flow<List<Movie>>

    // Избранные фильмы
    @Query("SELECT * FROM movies WHERE user_id = :userId AND is_favorite = 1")
    fun getFavoriteMovies(userId: Int): Flow<List<Movie>>


    @Query("SELECT * FROM movies WHERE user_id = :userId AND title LIKE '%' || :query || '%'")
    fun searchMovies(userId: Int, query: String): Flow<List<Movie>>


    @Query("SELECT COUNT(*) FROM movies WHERE user_id = :userId")
    suspend fun getMovieCount(userId: Int): Int

    @Query("SELECT AVG(rating) FROM movies WHERE user_id = :userId AND rating IS NOT NULL")
    suspend fun getAverageRating(userId: Int): Float?
}