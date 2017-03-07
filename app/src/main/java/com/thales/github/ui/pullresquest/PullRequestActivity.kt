package com.thales.github.ui.pullresquest

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView

import com.thales.github.Injection
import com.thales.github.R
import com.thales.github.model.pullrequest.PullRequest
import com.thales.github.utilities.Utility

class PullRequestActivity : AppCompatActivity(), PullRequestAdapter.RepositoryAdapterListener, PullRequestContract.View {

    private var progressBar: ProgressBar? = null
    private var messageDisplay: TextView? = null
    private var coordinatorLayout: CoordinatorLayout? = null

    private var presenter: PullRequestContract.Presenter? = null
    private var adapter: PullRequestAdapter? = null
    private var title: String? = null
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_request)

        initExtras()
        setupToolbar()
        setupRecyclerView()
        setFindViewById()

        initialize(savedInstanceState)
    }

    private fun initialize(savedInstanceState: Bundle?) {
        presenter = PullRequestPresenter(this, Injection.provideTasksRepository(applicationContext))

        if (savedInstanceState != null) {
            adapter!!.onRestoreInstanceState(savedInstanceState)
        }

        if (!adapter!!.hasItems()) presenter!!.loadPullRequests(title!!, username!!)
    }

    private fun initExtras() {
        title = intent.getStringExtra(EXTRA_TITLE)
        username = intent.getStringExtra(EXTRA_USERNAME)
    }

    private fun setupToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = title
    }

    private fun setFindViewById() {
        progressBar = findViewById(R.id.progress_bar) as ProgressBar
        messageDisplay = findViewById(R.id.tv_message_display) as TextView
        coordinatorLayout = findViewById(R.id.container) as CoordinatorLayout
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        adapter!!.onSaveInstanceState(outState)
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        adapter = PullRequestAdapter(this)
        recyclerView.adapter = adapter
    }

    override fun onClickListItem(pullRequest: PullRequest) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(pullRequest.htmlUrl)
        startActivity(i)
    }

    override fun showPullRequests(pullRequests: List<PullRequest>) {
        adapter!!.replaceData(pullRequests)
    }

    override fun showProgress() {
        progressBar!!.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar!!.visibility = View.GONE
    }

    override fun showSnackbarNoConnection() {
        Snackbar.make(coordinatorLayout!!, R.string.title_no_connection, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.title_retry) { presenter!!.loadPullRequests(title!!, username!!) }.show()
    }

    override fun showSnackbarError() {
        Snackbar.make(coordinatorLayout!!, R.string.error_message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.title_retry) { presenter!!.loadPullRequests(title!!, username = username!!) }.show()
    }

    override fun showNoRepositoriesMessage() {
        messageDisplay!!.setText(R.string.no_pull_request_message)
        messageDisplay!!.visibility = View.VISIBLE
    }

    override fun hideNoRepositoriesMessage() {
        messageDisplay!!.visibility = View.GONE
    }

    override fun isNetworkAvailable(): Boolean {
        return Utility.isNetworkAvailable(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private val EXTRA_TITLE = "title"
        private val EXTRA_USERNAME = "username"

        fun navigate(activity: Activity, title: String, username: String) {
            val it = Intent(activity, PullRequestActivity::class.java)
            it.putExtra(EXTRA_TITLE, title)
            it.putExtra(EXTRA_USERNAME, username)

            activity.startActivity(it)
        }
    }
}
