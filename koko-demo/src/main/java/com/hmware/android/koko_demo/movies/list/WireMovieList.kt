package com.hmware.android.koko_demo.movies.list

import com.hmware.android.koko_demo.movies.MovieCoordinator
import com.hmware.android.koko_demo.movies.details.MovieDetailsUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moviesListsModule = module {
    /*scope(named(MovieDetailsScope)) {
        scoped <MovieDetailsUseCase>{ (id: String) -> MovieDetailsUseCaseImpl(id) }
    }*/

    viewModel { MovieListViewModel() }

    factory { MovieCoordinator() }

    factory <MovieDetailsUseCase>{ (movieId: String) ->
        val coordinator = get<MovieCoordinator>()

        coordinator.createMovieDetailsUseCase(movieId)
    }
}