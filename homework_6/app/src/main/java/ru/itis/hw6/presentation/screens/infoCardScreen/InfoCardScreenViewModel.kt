package ru.itis.hw6.presentation.screens.infoCardScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.hw6.App
import ru.itis.hw6.R
import ru.itis.hw6.data.SongRepositoryImpl
import ru.itis.hw6.domain.GetSongDetailsUseCase
import ru.itis.hw6.domain.SongDetails

class InfoCardScreenViewModel(
    private val songId: Long
) : ViewModel() {

    private val repository = SongRepositoryImpl()
    private val getSongDetailsUseCase = GetSongDetailsUseCase(repository)

    private val _state = MutableStateFlow(InfoCardScreenState())
    val state = _state.asStateFlow()

    init {
        loadSongDetails()
    }

    fun loadSongDetails() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            try {
                val details = getSongDetailsUseCase(songId)
                _state.update {
                    it.copy(
                        isLoading = false,
                        songDetails = details
                    )
                }
            } catch (e: retrofit2.HttpException) {
                val errorMessage = when (e.code()) {
                    429 -> App.instance.getString(R.string.error_too_many_requests)
                    401 -> App.instance.getString(R.string.error_unauthorized)
                    404 -> App.instance.getString(R.string.error_not_found)
                    else -> App.instance.getString(R.string.error_server, e.code())
                }
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
            } catch (e: java.net.UnknownHostException) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = App.instance.getString(R.string.error_no_internet)
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = App.instance.getString(R.string.error_unknown, e.message ?: "Unknown")
                    )
                }
            }
        }
    }

    fun retry() {
        loadSongDetails()
    }
}

data class InfoCardScreenState(
    val isLoading: Boolean = false,
    val songDetails: SongDetails? = null,
    val error: String? = null
)