package com.lenakurasheva.notes.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lenakurasheva.notes.data.Repository
import com.lenakurasheva.notes.data.entity.User
import io.mockk.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SplashViewModelTest{
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()
    private val testUser = User("Ivan", "ivan@ivan")
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup(){
        clearAllMocks()
        coEvery { mockRepository.getCurrentUser() } returns testUser
        viewModel = SplashViewModel(mockRepository)
    }

    @Test
    fun `should invoke getCurrentUser once`() {
        viewModel.requestUser()
        coVerify(exactly = 1) { mockRepository.getCurrentUser() }
    }

    @Test
    fun `should call setData if current user is found`() {
      GlobalScope.launch {
          viewModel.requestUser()
          coVerify(exactly = 1) { viewModel.setData(any()) }
      }
    }

    @Test
    fun `should call setError if current user is not found`() {
        coEvery { mockRepository.getCurrentUser() } returns null
        GlobalScope.launch {
            viewModel.requestUser()
            coVerify(exactly = 1) { viewModel.setError(any()) }
        }
    }

}