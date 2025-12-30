package ru.itis.hw5.presentation.movie

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.itis.hw5.constants.SortOrder
import ru.itis.hw5.data.database.entities.Movie
import ru.itis.hw5.data.repository.MovieRepository

class MovieViewModel(
    private val movieRepository: MovieRepository,
    private val ownerId: Int
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    var searchQuery by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            isLoading = true
            movieRepository.getMoviesByUser(ownerId).collect { movies ->
                _movies.value = movies
                isLoading = false
            }
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            movieRepository.deleteMovie(movie)
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            movieRepository.toggleFavorite(movie.id, ownerId)
        }
    }

    fun search(query: String) {
        searchQuery = query
        viewModelScope.launch {
            movieRepository.searchMovies(ownerId, query).collect { movies ->
                _movies.value = movies
            }
        }
    }

    fun sortByRating() {
        viewModelScope.launch {
            movieRepository.getMoviesSortedByRating(userId = ownerId).collect {
                _movies.value = it
            }
        }

    }

    fun sortByDate() {
        viewModelScope.launch {
            movieRepository.getMoviesSortedByDate(userId = ownerId).collect {
                _movies.value = it
            }
        }
    }

    fun setSortOrder(order: SortOrder) {
        viewModelScope.launch {
            val flow = when (order) {
                SortOrder.RATING -> movieRepository.getMoviesSortedByRating(ownerId)
                SortOrder.DATE -> movieRepository.getMoviesSortedByDate(ownerId)
                SortOrder.TITLE -> movieRepository.getMoviesSortedByTitle(ownerId)
            }

            flow.collect { movies ->
                _movies.value = movies
            }
        }
    }
}