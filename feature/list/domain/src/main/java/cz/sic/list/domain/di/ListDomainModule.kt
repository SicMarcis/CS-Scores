package cz.sic.list.domain.di

import cz.sic.list.domain.usecase.TestDataUseCase
import cz.sic.list.domain.usecase.TestDataUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val listDomainModule = module {
    factoryOf(::TestDataUseCaseImpl) bind TestDataUseCase::class
}