package ru.itis.hw6.domain

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.itis.hw6.data.SongRepository

class GetSongDetailsUseCaseTest {

    private val repository: SongRepository = mockk()
    private lateinit var useCase: GetSongDetailsUseCase

    @Before
    fun setUp() {
        useCase = GetSongDetailsUseCase(repository)
    }

    @Test
    fun `invoke calls repository once and returns correct song details`() = runTest {
        val expected = SongDetails(
            id = 42L,
            title = "Bohemian Rhapsody",
            author = "Queen",
            album = "A Night at the Opera",
            releaseDate = "1975-10-31",
            imageUrl = "https://image.jpg",
            lyrics = "Is this the real life?"
        )
        coEvery { repository.getSongDetails(42L) } returns expected

        val result = useCase(42L)

        assertEquals(expected, result)
        coVerify(exactly = 1) { repository.getSongDetails(42L) }
    }
}
