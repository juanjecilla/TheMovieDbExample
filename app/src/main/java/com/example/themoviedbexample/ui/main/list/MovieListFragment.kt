package com.example.themoviedbexample.ui.main.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedbexample.R
import com.example.themoviedbexample.base.BaseScrollingView
import com.example.themoviedbexample.data.callbacks.OnMovieItemListener
import com.example.themoviedbexample.data.model.MovieItem
import com.example.themoviedbexample.data.model.messages.FavUpdatedMessage
import com.example.themoviedbexample.ui.detail.DetailActivity
import com.example.themoviedbexample.utils.EndlessRecyclerViewScrollListener
import com.example.themoviedbexample.utils.Properties
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.android.synthetic.main.fragment_movie_list.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class MovieListFragment : BaseScrollingView(), MovieListContract.View, OnMovieItemListener {


    @Inject
    lateinit var mPresenter: MovieListPresenter

    private lateinit var mAdapterMovie: MovieListAdapter
    private var mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    private var mEndlessRecyclerViewScrollListener =
        object : EndlessRecyclerViewScrollListener(mLayoutManager, STARTING_PAGE_INDEX) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                mPresenter.getMovies(page)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)

        mAdapterMovie = MovieListAdapter(arrayListOf(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.movie_list.adapter = mAdapterMovie
        view.movie_list.isNestedScrollingEnabled = false
        mLayoutManager.stackFromEnd = false
        view.movie_list.layoutManager = mLayoutManager
        view.movie_list.addOnScrollListener(mEndlessRecyclerViewScrollListener)

        mPresenter.getMovies(STARTING_PAGE_INDEX)
    }

    override fun onStart() {
        super.onStart()
        mPresenter.onViewActive(this)
    }

    override fun onStop() {
        mPresenter.onViewInactive()
        super.onStop()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun shouldShowPlaceholderText() {
        if (mAdapterMovie.itemCount > 1) {
            empty_list.visibility = View.GONE
            movie_list.visibility = View.VISIBLE
        } else {
            empty_list.visibility = View.VISIBLE
            movie_list.visibility = View.GONE
        }
    }

    override fun resetState() {
        movie_list.smoothScrollToPosition(0)
    }

    override fun showMovies(movieItems: ArrayList<MovieItem>) {
        mAdapterMovie.addAll(movieItems)
    }

    override fun showMovieDetail(movieItem: MovieItem) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(Properties.EXTRA_MOVIE, movieItem)
        startActivity(intent)
    }

    override fun updateFav(movieItem: MovieItem) {
        mPresenter.updateFavourite(movieItem)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: FavUpdatedMessage) {
        mAdapterMovie.notifyItemUpdated(event.item)
    }

    companion object {

        private const val STARTING_PAGE_INDEX = 0

        fun newInstance(): MovieListFragment {
            val fragment = MovieListFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}