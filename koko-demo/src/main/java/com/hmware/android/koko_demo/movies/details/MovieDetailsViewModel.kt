package com.hmware.android.koko_demo.movies.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class MovieVVO(
        val title: String,
        val description: String,
        val isFavorite: LiveData<Boolean>
)

interface MovieDetailsUseCase {
    val movieVVO: MovieVVO

    fun onAddMovieToFavorites()
    fun onRemoveMovieFromFavorites()
}

class MovieDetailsViewModel(
        val movieUseCase: MovieDetailsUseCase
) : ViewModel(){

    val movieVVO: MovieVVO
        get() = movieUseCase.movieVVO

    fun onAddMovieToFavorites() {
        movieUseCase.onAddMovieToFavorites()
    }

    fun onRemoveMovieFromFavorites() {
        movieUseCase.onRemoveMovieFromFavorites()
    }
}