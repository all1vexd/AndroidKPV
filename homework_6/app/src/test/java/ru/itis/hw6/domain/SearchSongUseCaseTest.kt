package ru.itis.hw6.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.itis.hw6.data.SongRepository

class SearchSongUseCaseTest {

    private val repository: SongRepository = mockk()
    private lateinit var useCase: SearchSongUseCase

    @Before
    fun setUp() {
        useCase = SearchSongUseCase(repository)
    }

    @Test
    fun `invoke calls repository once and returns correct list for given query`(): Unit = runTest {
        val songs = listOf(Song(1L, "Bohemian Rhapsody", "Queen", "https://thumb.jpg"))
        every { repository.searchSong("queen") } returns flowOf(songs)

        val result = useCase("queen").first()

        assertEquals(songs, result)
        verify(exactly = 1) { repository.searchSong("queen") }
    }

    @Test
    fun `invoke returns empty list when query is empty`() = runTest {
        every { repository.searchSong("") } returns flowOf(emptyList())

        val result = useCase("").first()

        assertTrue(result.isEmpty())
    }
}
