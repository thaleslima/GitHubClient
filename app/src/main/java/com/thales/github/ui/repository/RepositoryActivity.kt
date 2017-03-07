package com.thales.github.ui.repository

import android.os.Bundle

import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View

import com.thales.github.Injection
import com.thales.github.R
import com.thales.github.model.repository.Repository
import com.thales.github.ui.listener.EndlessRecyclerOnScrollListener
import com.thales.github.ui.pullresquest.PullRequestActivity
import com.thales.github.utilities.Utility
import com.thales.github.utilities.schedulers.SchedulerProvider
import kotlinx.android.synthetic.main.activity_repository.*
import kotlinx.android.synthetic.main.loading_message_include.*

class RepositoryActivity : AppCompatActivity(), RepositoryAdapter.RepositoryAdapterListener, RepositoryContract.View {
    private var adapter: RepositoryAdapter? = null
    private var scrollListener: EndlessRecyclerOnScrollListener? = null
    private var presenter: RepositoryContract.Presenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)

        setupToolbar()
        setupRecyclerView()

        initialize(savedInstanceState)
    }

    private fun setupToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    public override fun onPause() {
        super.onPause()
        presenter!!.unsubscribe()
    }

    private fun initialize(savedInstanceState: Bundle?) {
        presenter = RepositoryPresenter(this,
                Injection.provideTasksRepository(applicationContext),
                SchedulerProvider.getInstance())

        if (savedInstanceState != null) {
            adapter!!.onRestoreInstanceState(savedInstanceState)
            scrollListener!!.onRestoreInstanceState(savedInstanceState)
        }

        if (!adapter!!.hasItems()) {
            scrollListener!!.setCurrentPage(1)
            presenter!!.loadRepositories()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        adapter!!.onSaveInstanceState(outState)
        scrollListener!!.onSaveInstanceState(outState)
    }

    private fun setupRecyclerView() {
        recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.layoutManager = layoutManager

        adapter = RepositoryAdapter(this)
        recycler_view.adapter = adapter

        scrollListener = object : EndlessRecyclerOnScrollListener(layoutManager) {
            override fun onLoadMore(currentPage: Int) {
                presenter!!.setCurrentPage(currentPage)
                presenter!!.loadRepositories()
            }
        }

        recycler_view.addOnScrollListener(scrollListener)
    }

    override fun onClickListItem(repository: Repository) {
        presenter!!.openPullRequest(repository)
    }

    override fun showRepositories(repositories: List<Repository>) {
        adapter!!.addData(repositories)
    }

    override fun showProgress() {
        progress_bar!!.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress_bar!!.visibility = View.GONE
    }

    override fun showProgressList() {
        progress_bar_recycler!!.visibility = View.VISIBLE
    }

    override fun hideProgressList() {
        progress_bar_recycler!!.visibility = View.GONE
    }

    override fun showPullRequestUi(repository: Repository) {
        PullRequestActivity.navigate(this, repository.name!!, repository.loginUser!!)
    }

    override fun showSnackbarNoConnection() {
        Snackbar.make(container!!, R.string.title_no_connection, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.title_retry) { presenter!!.loadRepositories() }.show()
    }

    override fun showSnackbarError() {
        Snackbar.make(container!!, R.string.error_message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.title_retry) { presenter!!.loadRepositories() }.show()
    }

    override fun showNoRepositoriesMessage() {
        tv_message_display!!.setText(R.string.no_movies_message)
        tv_message_display!!.visibility = View.VISIBLE
    }

    override fun hideNoRepositoriesMessage() {
        tv_message_display!!.visibility = View.GONE
    }

    override fun isNetworkAvailable(): Boolean {
        return Utility.isNetworkAvailable(this)
    }
}
