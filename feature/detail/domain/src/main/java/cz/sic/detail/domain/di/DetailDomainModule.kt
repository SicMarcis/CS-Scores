package cz.sic.detail.domain.di

import cz.sic.detail.domain.usecase.ChangeScoresUseCase
import cz.sic.detail.domain.usecase.ChangeScoresUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val detailDomainModule = module {
    factoryOf(::ChangeScoresUseCaseImpl) bind ChangeScoresUseCase::class
}