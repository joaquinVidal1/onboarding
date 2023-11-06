package com.example.Store.domain.usecase.base

import kotlinx.coroutines.flow.Flow

interface ObserveUseCase<T> {
    operator fun invoke(): Flow<T>
}