package com.lenakurasheva.notes.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.lenakurasheva.notes.data.Repository
import com.lenakurasheva.notes.data.entity.User
import com.lenakurasheva.notes.data.errors.NoAuthException
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SplashViewModelTest{
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository = mockk<Repository>()
    private val currentUserLiveData  = MutableLiveData<User?>()
    private val testUser = User("Ivan", "ivan@ivan")
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup(){
        clearAllMocks()
//        every {mockRepository.getCurrentUser()} returns currentUserLiveData
        viewModel = SplashViewModel(mockRepository)
    }

    @Test
    fun `should invoke getCurrentUser once`() {
//        viewModel.requestUser()
//        verify(exactly = 1) { mockRepository.getCurrentUser() }
    }

    @Test
    fun `should return SplashViewState auth true`() {
//        var result: Boolean? = null
//        viewModel.viewStateLiveData.observeForever{
//            result = it.data
//        }
//        viewModel.requestUser()
//        currentUserLiveData.value = testUser
//        Assert.assertEquals(result, true)
    }

    @Test
    fun `should return auth error`() {
//        var result: Throwable? = null
//        viewModel.viewStateLiveData.observeForever{
//            result = it.error
//        }
//        viewModel.requestUser()
//        currentUserLiveData.value = null
//        assertEquals(result?.javaClass,  NoAuthException::class.java)
    }

    @Test
    fun `should remove observer`() {
//        viewModel.onCleared()
//        Assert.assertFalse(currentUserLiveData.hasObservers())
    }
}