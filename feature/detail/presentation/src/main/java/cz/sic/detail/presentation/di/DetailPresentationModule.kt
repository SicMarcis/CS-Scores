package cz.sic.detail.presentation.di

import cz.sic.detail.presentation.vm.ScoreDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val detailPresentationModule = module {
    viewModelOf(::ScoreDetailViewModel)
}