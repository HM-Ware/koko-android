package com.hmware.android.koko_demo.movies.details

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module


val movieDetailsModule = module {
    // ViewModel for Detail View
    viewModel { (id: String) -> MovieDetailsViewModel(get { parametersOf(id) }) }
}