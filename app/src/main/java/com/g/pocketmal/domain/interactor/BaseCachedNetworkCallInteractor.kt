package com.g.pocketmal.domain.interactor

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseCachedNetworkCallInteractor<T, V>(
        protected val executionContext: CoroutineDispatcher = Dispatchers.IO,
        protected val resultContext: CoroutineDispatcher = Dispatchers.Main
) : CoroutineScope {

    protected abstract suspend fun executeCache(input: T): V

    protected abstract suspend fun executeNetwork(input: T): V

    private var onCacheResult: ((V) -> Unit)? = null
    private var onNetworkResult: ((V) -> Unit)? = null
    private var onCacheError: ((Throwable) -> Unit)? = null
    private var onNetworkError: ((Throwable) -> Unit)? = null
    private var onComplete: (() -> Unit)? = null

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = executionContext + job

    fun execute(
            input: T,
            skipCache: Boolean = false,
            skipNetwork: Boolean = false,
            onCacheResult: ((V) -> Unit)? = null,
            onNetworkResult: ((V) -> Unit)? = null,
            onCacheError: ((Throwable) -> Unit)? = null,
            onNetworkError: ((Throwable) -> Unit)? = null,
            onComplete: (() -> Unit)? = null
    ) {
        this.onCacheResult = onCacheResult
        this.onNetworkResult = onNetworkResult
        this.onCacheError = onCacheError
        this.onNetworkError = onNetworkError
        this.onComplete = onComplete
        launch {
            executeSafe(input, skipCache, skipNetwork)
        }
    }

    fun cancel() {
        job.cancel()
        onCacheResult = null
        onNetworkResult = null
        onCacheError = null
        onNetworkError = null
        onComplete = null
    }

    private suspend fun executeSafe(input: T, skipCache: Boolean, skipNetwork: Boolean) {

        if (!skipCache) {
            try {
                val result = executeCache(input)
                runInResultContext { onCacheResult?.invoke(result) }
            } catch (throwable: Throwable) {
                runInResultContext { onCacheError?.invoke(throwable) }
            }
        }

        if (!skipNetwork) {
            try {
                val result = executeNetwork(input)
                runInResultContext { onNetworkResult?.invoke(result) }
            } catch (throwable: Throwable) {
                runInResultContext { onNetworkError?.invoke(throwable) }
            }
        }

        runInResultContext { onComplete?.invoke() }
    }

    private fun runInResultContext(block: () -> Unit) = launch(resultContext) {
        block()
    }
}
