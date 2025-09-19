package cz.sic.list.domain.di

import cz.sic.list.domain.usecase.ChangeScoresUseCase
import cz.sic.list.domain.usecase.GetAllScoresUseCase
import org.koin.dsl.module

val listDomainModule = module {
    // Use Cases
    factory { GetAllScoresUseCase(get()) }
    factory { ChangeScoresUseCase(get()) }
}