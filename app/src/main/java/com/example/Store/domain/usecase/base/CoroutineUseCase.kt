package com.example.proyecto_final_de_onboarding.domain.usecase.base

import com.example.Store.di.DispatcherModule
import com.example.proyecto_final_de_onboarding.data.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

abstract class CoroutineUseCase<in P, R>() {

    @Inject
    @DispatcherModule.IoDispatcher lateinit var coroutineDispatcher: CoroutineDispatcher
    suspend operator fun invoke(params: P): Result<R> = try {
        withContext(coroutineDispatcher) {
            execute(params).let {
                Result.Success(it)
            }
        }
    } catch (throwable: Throwable) {
        Result.Error(throwable.message, throwable)
    }

    protected abstract suspend fun execute(params: P): R
}
