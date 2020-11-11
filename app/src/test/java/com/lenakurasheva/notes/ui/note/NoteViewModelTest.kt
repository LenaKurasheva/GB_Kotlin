package com.lenakurasheva.notes.ui.note

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.lenakurasheva.notes.data.Repository
import com.lenakurasheva.notes.data.entity.Note
import com.lenakurasheva.notes.data.model.NoteResult
import io.mockk.*
import kotlinx.coroutines.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.doReturn
import kotlin.coroutines.CoroutineContext

class NoteViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()

    private val testNote = Note("1", "title1", "text1")
    private val testError = Throwable()

    private lateinit var viewModel: NoteViewModel

    @Before
    fun setup() {
        clearAllMocks()
        coEvery { mockRepository.getNoteById(testNote.id) } returns testNote
        coEvery { mockRepository.deleteNote(testNote.id) } returns Unit
        coEvery { mockRepository.saveNote(any()) } returns testNote
        viewModel = NoteViewModel(mockRepository)
    }

    @Test
    fun `loadNote should call setData`()  {
        GlobalScope.launch {
            viewModel.loadNote(testNote.id)
            verify(exactly = 1) { viewModel.setData(any()) }
        }
    }

    @Test
    fun `loadNote should call setError`() {
        coEvery { mockRepository.getNoteById(testNote.id) } throws testError
        GlobalScope.launch {
            viewModel.loadNote(testNote.id)
            verify(exactly = 1) { viewModel.setError(testError) }
        }
    }

    @Test
    fun `deleteNote should call setData`() {
        viewModel.saveChanges(testNote)
        GlobalScope.launch {
            viewModel.deleteNote()
            verify(exactly = 1) { viewModel.setData(any()) }
            assertTrue(viewModel.pendingNote == null)
        }
    }

    @Test
    fun `deleteNote should call setError if deleteNote throws ex`() {
        coEvery { mockRepository.getNoteById(testNote.id) } throws  testError
        viewModel.saveChanges(testNote)
        GlobalScope.launch {
            viewModel.deleteNote()
            verify(exactly = 1) { viewModel.setError(testError) }
        }
    }

    @Test
    fun `repository should save changes in onCleared`() {
        viewModel.saveChanges(testNote)
        viewModel.onCleared()
        coVerify(exactly = 1) { mockRepository.saveNote(testNote) }
    }

}