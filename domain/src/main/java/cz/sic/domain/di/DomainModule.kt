package cz.sic.domain.di

import cz.sic.domain.usecase.GetAllScoresUseCase
import org.koin.dsl.module

val domainModule = module {
    // Use Cases
    factory { GetAllScoresUseCase(get()) }
    //factory { ChangeScoresUseCase(get()) }
}