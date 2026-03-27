package ru.itis.hw6.presentation.screens.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.hw6.App
import ru.itis.hw6.R
import ru.itis.hw6.data.SongRepositoryImpl
import ru.itis.hw6.domain.SearchSongUseCase
import ru.itis.hw6.domain.Song

class MainScreenViewModel(
    private val searchSongUseCase: SearchSongUseCase = SearchSongUseCase(SongRepositoryImpl())
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    private val searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(500)
                .filter { it.isNotBlank() }
                .collectLatest { query ->
                    searchSongs(query)
                }
        }
    }

    fun processCommand(command: MainScreenCommand) {
        when (command) {
            is MainScreenCommand.InputSearchQuery -> {
                val newQuery = command.query.trim()
                _state.update {
                    it.copy(
                        query = newQuery,
                        error = null
                    )
                }
                searchQuery.update { newQuery }
            }
        }
    }

    private fun searchSongs(query: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true, error = null
                )
            }

            try {
                var hasResult = false
                searchSongUseCase(query).collect { songs ->
                    hasResult = true
                    _state.update {
                        it.copy(
                            isLoading = false,
                            searchedSongs = songs,
                            error = if (songs.isEmpty()) {
                                App.instance.getString(R.string.nothing_found)
                            } else null
                        )
                    }
                }

                if (!hasResult) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            searchedSongs = emptyList(),
                            error = App.instance.getString(R.string.nothing_found)
                        )
                    }
                }

            } catch (e: retrofit2.HttpException) {
                val errorMessage = when (e.code()) {
                    429 -> App.instance.getString(R.string.error_too_many_requests)
                    401 -> App.instance.getString(R.string.error_unauthorized)
                    404 -> App.instance.getString(R.string.error_service_not_found)
                    else -> App.instance.getString(R.string.error_server, e.code())
                }
                _state.update {
                    it.copy(
                        isLoading = false,
                        searchedSongs = emptyList(),
                        error = errorMessage
                    )
                }
            } catch (e: java.net.UnknownHostException) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        searchedSongs = emptyList(),
                        error = App.instance.getString(R.string.error_no_internet)
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        searchedSongs = emptyList(),
                        error = App.instance.getString(R.string.error_unknown, e.message ?: "Unknown")
                    )
                }
            }
        }
    }

    fun retry() {
        val currentQuery = _state.value.query
        if (currentQuery.isNotBlank()) {
            searchSongs(currentQuery)
        }
    }
}

sealed interface MainScreenCommand {
    data class InputSearchQuery(val query: String): MainScreenCommand
}

data class MainScreenState(
    val query: String = "",
    val searchedSongs: List<Song> = listOf(),
    val isLoading: Boolean = false,
    val error: String? = null
)