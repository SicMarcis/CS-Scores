package cz.sic.list.data.di

import cz.sic.list.domain.di.listDomainModule
import org.koin.dsl.module

val listDataModule = module {
    includes(listDomainModule)

}