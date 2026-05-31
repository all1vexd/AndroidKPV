package ru.itis.hw6.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody.Companion.toResponseBody
import org.jsoup.Jsoup
import retrofit2.HttpException
import retrofit2.Response
import ru.itis.hw6.data.network.GeniusApi
import ru.itis.hw6.domain.Song
import ru.itis.hw6.domain.SongDetails

class SongRepositoryImpl(private val api: GeniusApi) : SongRepository {


    override fun searchSong(query: String): Flow<List<Song>> = flow {

        delay(1000L)

        val response = api.getSongs(query)

        val songs = response.response.hits.map { hit ->
            hit.result.toEntity()
        }

        emit(value = songs)
    }

    override suspend fun getSongDetails(id: Long): SongDetails {

        delay(1000L)

        if (ErrorSimulator.shouldSimulateError()) {
            throw HttpException(
                Response.error<Any>(
                    404,
                    "{\"error\":\"Not Found\"}".toResponseBody(null)
                )
            )
        }

        val response = api.getSongById(id)

        val html = withContext(Dispatchers.IO) {
            Jsoup
                .connect(response.response.song.lyricsPageUrl)
                .get()
        }
        val lyricsContainers = html.select("div[data-lyrics-container=true]")

        lyricsContainers.forEach { container ->
            container.select("div[data-exclude-from-selection=true]").remove()
        }

        val lyrics = lyricsContainers
            .map { container ->
                val paragraphs = container.select("p")
                if (paragraphs.isNotEmpty()) {
                    paragraphs.joinToString("\n") { it.text() }
                } else {
                    container.text()
                }
            }
            .joinToString("\n")
            .trim()
            .ifEmpty { "Текст не найден" }

        val songDetails = response.response.song.toEntity(lyrics)

        return songDetails
    }
}