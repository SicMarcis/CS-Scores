package cz.sic.detail.domain.di

import cz.sic.detail.domain.usecase.ChangeScoresUseCase
import org.koin.dsl.module

val detailDomainModule = module {
    factory { ChangeScoresUseCase(get()) }
}