package com.hmware.android.koko_demo.movies.details

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.hmware.android.koko_demo.R
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a [MovieListActivity]
 * in two-pane mode (on tablets) or a [MovieDetailActivity]
 * on handsets.
 */
class MovieDetailFragment : Fragment(R.layout.movie_detail)/*, KoinScopeComponent */{

/*
    override val scope : Scope by lazy {
        getKoin().createScope(MovieDetailsScope,named(MovieDetailsScope))
    }
*/
    private val movieDetailsViewModel: MovieDetailsViewModel  by viewModel {
        parametersOf(arguments?.getString(ARG_ITEM_ID))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title = movieDetailsViewModel.movieVVO.title
        view.findViewById<TextView>(R.id.movie_detail).text = movieDetailsViewModel.movieVVO.description
    }

    override fun onDestroy() {
        //scope.close()
        super.onDestroy()
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        private const val ARG_ITEM_ID = "item_id"

        fun newInstance(movieId: String) = MovieDetailFragment().apply {
            arguments = Bundle().apply {
                putString(MovieDetailFragment.ARG_ITEM_ID, movieId)
            }
        }

    }
}