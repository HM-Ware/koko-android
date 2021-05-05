package com.hmware.android.koko_demo.movies.list

import androidx.lifecycle.MutableLiveData
import com.hmware.android.koko_demo.movies.details.MovieDetailsUseCase
import com.hmware.android.koko_demo.movies.details.MovieVVO

class MovieDetailsUseCaseImpl(
        private val movie: MoviesContent.Movie,
        private val addMovieToFavorites : (MoviesContent.Movie) -> Unit,
        private val removeMovieFromFavorites : (MoviesContent.Movie) -> Unit
) : MovieDetailsUseCase {

    private val isFavoriteLiveData = MutableLiveData<Boolean>()


    override val movieVVO: MovieVVO = MovieVVO(
            title = movie.content,
            description = movie.details,
            isFavorite = isFavoriteLiveData
    )

    override fun onAddMovieToFavorites() {
        addMovieToFavorites(movie)
    }

    override fun onRemoveMovieFromFavorites() {
        removeMovieFromFavorites(movie)
    }
}