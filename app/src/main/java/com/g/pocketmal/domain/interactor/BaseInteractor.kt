package com.g.pocketmal.domain.interactor

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseInteractor<T, V>(
        protected val executionContext: CoroutineDispatcher = Dispatchers.IO,
        protected val resultContext: CoroutineDispatcher = Dispatchers.Main
) : CoroutineScope {

    protected abstract suspend fun execute(input: T): V

    private var onResult: ((V) -> Unit)? = null
    private var onError: ((Throwable) -> Unit)? = null
    private var onComplete: (() -> Unit)? = null

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = executionContext + job

    fun execute(input: T, onResult: ((V) -> Unit)? = null, onError: ((Throwable) -> Unit)? = null, onComplete: (() -> Unit)? = null) {
        this.onResult = onResult
        this.onError = onError
        this.onComplete = onComplete
        launch {
            executeSafe(input)
        }
    }

    fun cancel() {
        job.cancel()
        onResult = null
        onError = null
        onComplete = null
    }

    private suspend fun executeSafe(input: T) {
        try {
            val result = execute(input)
            runInResultContext { onResult?.invoke(result) }
        } catch (throwable: Throwable) {
            runInResultContext { onError?.invoke(throwable) }
        }
        runInResultContext { onComplete?.invoke() }
    }

    private fun runInResultContext(block: () -> Unit) = launch(resultContext) {
        block()
    }
}
