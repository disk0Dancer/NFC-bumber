package com.nfcbumber.data.di

import com.nfcbumber.data.repository.CardRepositoryImpl
import com.nfcbumber.domain.repository.CardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for repository dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCardRepository(
        cardRepositoryImpl: CardRepositoryImpl
    ): CardRepository
}
