package cz.sic.list.domain.di

import cz.sic.list.domain.usecase.TestDataUseCase
import org.koin.dsl.module

val listDomainModule = module {
    factory { TestDataUseCase(get()) }
}