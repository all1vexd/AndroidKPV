package ru.itis.hw6.presentation.screens.infoCardScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.hw6.data.SongRepository
import ru.itis.hw6.data.SongRepositoryImpl
import ru.itis.hw6.domain.GetSongDetailsUseCase
import ru.itis.hw6.domain.SongDetails

class InfoCardScreenViewModel(
    private val getSongDetailsUseCase: GetSongDetailsUseCase = GetSongDetailsUseCase(SongRepositoryImpl()),
    private val songId: Long
): ViewModel() {

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
                    429 -> "Слишком много запросов. Подождите немного и попробуйте снова."
                    401 -> "Ошибка авторизации. Проверьте API ключ."
                    404 -> "Песня не найдена."
                    else -> "Ошибка сервера: ${e.code()}"
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
                        error = "Нет подключения к интернету. Проверьте соединение."
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Ошибка: ${e.message}"
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