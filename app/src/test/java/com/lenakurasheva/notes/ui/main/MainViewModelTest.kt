package com.lenakurasheva.notes.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lenakurasheva.notes.data.Repository
import com.lenakurasheva.notes.data.entity.Note
import com.lenakurasheva.notes.data.model.NoteResult
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.launch

class MainViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()
    private val repositoryNotes = Channel<NoteResult>() // = repositoryNotes
    val testData = listOf(Note("1"), Note("2"))
    val testNoteResultSuccess = NoteResult.Success(testData)
    val testError = Throwable()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        clearAllMocks()
        every { mockRepository.getNotes() } returns repositoryNotes
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `should getNotes once`() {
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `init should call setData`() {
        GlobalScope.launch {
            repositoryNotes.send(testNoteResultSuccess)
            verify() { viewModel.setData(testData) }
        }
    }

    @Test
    fun `init should call setError`() {
        GlobalScope.launch {
            repositoryNotes.send(NoteResult.Error(testError))
            verify() { viewModel.setError(testError) }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should cancel repositoryNotes channel`() {
        viewModel.onCleared()
        assertTrue(repositoryNotes.isClosedForReceive)
        assertTrue(repositoryNotes.isClosedForSend)
    }
}
