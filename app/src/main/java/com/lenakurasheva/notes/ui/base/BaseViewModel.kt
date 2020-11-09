package com.lenakurasheva.notes.ui.base

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.CoroutineContext

open class BaseViewModel <S>: ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext by lazy{
        Dispatchers.Default + Job()
    }

    private val viewStateChannel = BroadcastChannel<S>(Channel.CONFLATED)
    private val errorChannel = Channel<Throwable>()

    fun getViewState(): ReceiveChannel<S> = viewStateChannel.openSubscription()
    fun getErrorChannel(): ReceiveChannel<Throwable> = errorChannel

    @VisibleForTesting
    fun setError(e: Throwable) = launch{
        errorChannel.send(e)
    }

    @VisibleForTesting
    fun setData(data: S) = launch {
        viewStateChannel.send(data)
    }

    override fun onCleared() {
        super.onCleared()
        viewStateChannel.close()
        errorChannel.close()
        coroutineContext.cancel()
    }
}