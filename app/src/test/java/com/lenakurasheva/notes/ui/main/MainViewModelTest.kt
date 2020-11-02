package com.lenakurasheva.notes.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.lenakurasheva.notes.data.Repository
import com.lenakurasheva.notes.data.entity.Note
import com.lenakurasheva.notes.data.model.NoteResult
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()
    private val repositoryNotesLiveData = MutableLiveData<NoteResult>() // = repositoryNotes

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        clearAllMocks()
        every { mockRepository.getNotes() } returns repositoryNotesLiveData
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `should getNotes once`() {
        verify(exactly = 1) { mockRepository.getNotes() }
    }

    @Test
    fun `should return Notes`() {
        var result: List<Note>? = null
        val testData = listOf(Note("1"), Note("2"))
        viewModel.viewStateLiveData.observeForever {
            result = it.data
        }
        repositoryNotesLiveData.value = NoteResult.Success(testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")
        viewModel.viewStateLiveData.observeForever {
            result = it.error
        }
        repositoryNotesLiveData.value = NoteResult.Error(error = testData)
        assertEquals(testData, result)
    }


    @Test
    fun `should remove observer`() {
        viewModel.onCleared()
        assertFalse(repositoryNotesLiveData.hasObservers())
    }
}
