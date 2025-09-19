package cz.sic.list.presentation.di

import cz.sic.list.presentation.vm.ScoresListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val listPresentationModule = module {
    viewModelOf(::ScoresListViewModel)
}