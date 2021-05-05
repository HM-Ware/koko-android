package com.hmware.android.koko_demo.movies.list

import androidx.lifecycle.ViewModel

class MovieListViewModel : ViewModel() {
    val movies: MutableList<MoviesContent.Movie>
        get() = MoviesContent.ITEMS
}