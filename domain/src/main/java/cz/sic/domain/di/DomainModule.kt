package cz.sic.domain.di

import cz.sic.domain.usecase.DeleteScoreUseCase
import cz.sic.domain.usecase.DeleteScoreUseCaseImpl
import cz.sic.domain.usecase.GetAllScoresUseCase
import cz.sic.domain.usecase.GetAllScoresUseCaseImpl
import cz.sic.domain.usecase.GetScoreItemUseCase
import cz.sic.domain.usecase.GetScoreItemUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    // Use Cases
    factoryOf(::GetAllScoresUseCaseImpl) bind GetAllScoresUseCase::class
    factoryOf(::GetScoreItemUseCaseImpl) bind GetScoreItemUseCase::class
    factoryOf(::DeleteScoreUseCaseImpl) bind DeleteScoreUseCase::class
}