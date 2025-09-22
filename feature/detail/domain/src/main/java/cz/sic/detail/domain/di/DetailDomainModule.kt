package cz.sic.detail.domain.di

import cz.sic.detail.domain.usecase.DeleteAllScoresUseCase
import cz.sic.detail.domain.usecase.DeleteAllScoresUseCaseImpl
import cz.sic.detail.domain.usecase.SaveScoreUseCase
import cz.sic.detail.domain.usecase.SaveScoreUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val detailDomainModule = module {
    factoryOf(::SaveScoreUseCaseImpl) bind SaveScoreUseCase::class
    factoryOf(::DeleteAllScoresUseCaseImpl) bind DeleteAllScoresUseCase::class
}