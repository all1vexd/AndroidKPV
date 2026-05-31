package ru.itis.hw6.presentation.screens.mainScreen

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.itis.hw6.App
import ru.itis.hw6.domain.SearchSongUseCase
import ru.itis.hw6.domain.Song

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val searchSongUseCase: SearchSongUseCase = mockk()
    private lateinit var viewModel: MainScreenViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val mockApp = mockk<App>(relaxed = true)
        every { mockApp.getString(any()) } returns "Nothing found"
        mockkObject(App.Companion)
        every { App.instance } returns mockApp

        viewModel = MainScreenViewModel(searchSongUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `processCommand with non-empty result updates songs list in state`() = runTest(testDispatcher) {
        val songs = listOf(
            Song(1L, "Bohemian Rhapsody", "Queen", "https://thumb.jpg"),
            Song(2L, "We Will Rock You", "Queen", "https://thumb2.jpg")
        )
        every { searchSongUseCase(any()) } returns flowOf(songs)

        viewModel.processCommand(MainScreenCommand.InputSearchQuery("queen"))
        advanceTimeBy(501)

        assertEquals(songs, viewModel.state.value.searchedSongs)
        assertNull(viewModel.state.value.error)
        assertTrue(!viewModel.state.value.isLoading)
    }

    @Test
    fun `processCommand with empty result sets error message in state`() = runTest(testDispatcher) {
        every { searchSongUseCase(any()) } returns flowOf(emptyList())

        viewModel.processCommand(MainScreenCommand.InputSearchQuery("xyzabc123"))
        advanceTimeBy(501)

        assertTrue(viewModel.state.value.searchedSongs.isEmpty())
        assertNotNull(viewModel.state.value.error)
    }
}
