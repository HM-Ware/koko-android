package com.hmware.android.koko_demo.movies

import com.hmware.android.koko_demo.movies.list.MovieDetailsUseCaseImpl
import com.hmware.android.koko_demo.movies.list.MoviesContent

class MovieCoordinator {

    fun createMovieDetailsUseCase(movieId: String) =
        MovieDetailsUseCaseImpl(
                MoviesContent.ITEM_MAP[movieId]!!,
                ::onAddMovieToFavorites,
                ::onRemoveMovieFromFavorites
        )


    fun onAddMovieToFavorites(movie: MoviesContent.Movie) {
        MoviesContent.FAVORITE_MOVIE_MAP[movie.id] = true
    }

    fun onRemoveMovieFromFavorites(movie: MoviesContent.Movie) {
        MoviesContent.FAVORITE_MOVIE_MAP[movie.id] = false
    }
}