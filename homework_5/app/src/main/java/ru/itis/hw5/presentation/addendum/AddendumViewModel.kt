package ru.itis.hw5.presentation.addendum

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.itis.hw5.constants.StatusConstant
import ru.itis.hw5.constants.StringResources
import ru.itis.hw5.data.database.entities.Movie
import ru.itis.hw5.data.repository.MovieRepository

class AddendumViewModel(
    private val movieRepository: MovieRepository,
    private val ownerId: Int,
    private val movieId: Int?
): ViewModel() {

    var name by mutableStateOf("")
    var creator by mutableStateOf("")
    var rating by mutableStateOf("")
    var selectedStatus by mutableStateOf(StatusConstant.STATUS_PLANNED)

    var error by mutableStateOf<String?>(null)
    var isSaving by mutableStateOf(false)

    init {
        movieId?.let { id ->
            viewModelScope.launch {
                movieRepository.getMovieById(id, ownerId)?.let { movie ->
                    name = movie.title
                    creator = movie.director ?: ""
                    rating = movie.rating?.toString() ?: ""
                    selectedStatus = movie.status
                }
            }
        }
    }

    fun saveMovie(onSuccess:() -> Unit) {
        val ratingValue = rating.replace(",", ".").toFloatOrNull()

        if (name.isBlank()) {
            error = StringResources.ERROR_TITLE_EMPTY
            return
        }
        if (creator.isBlank()) {
            error = StringResources.ERROR_DIRECTOR_EMPTY
            return
        }
        if (ratingValue == null || ratingValue !in 0f..10f) {
            error = StringResources.ERROR_RATING_RANGE
            return
        }

        viewModelScope.launch {
            isSaving = true
            val success = if (movieId == null) {
                val newMovie = Movie().apply {
                    this.userId = ownerId
                    this.title = name
                    this.director = creator
                    this.rating = ratingValue
                    this.status = selectedStatus
                }
                movieRepository.addMovie(newMovie)
            } else {
                movieRepository.getMovieById(movieId, ownerId)?.let { movie ->
                    movie.title = name
                    movie.director = creator
                    movie.rating = ratingValue
                    movie.status = selectedStatus
                    movieRepository.updateMovie(movie)
                } ?: false
            }

            if (success) {
                onSuccess()
            } else {
                error = StringResources.ERROR_SAVING_DB
            }
            isSaving = false
        }
    }
}