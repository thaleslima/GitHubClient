package com.thales.github.ui.repository;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thales.github.Injection;
import com.thales.github.R;
import com.thales.github.model.repository.Repository;
import com.thales.github.ui.listener.EndlessRecyclerOnScrollListener;
import com.thales.github.ui.pullresquest.PullRequestActivity;
import com.thales.github.utilities.Utility;
import com.thales.github.utilities.schedulers.SchedulerProvider;

import java.util.List;

public class RepositoryActivity extends AppCompatActivity
        implements RepositoryAdapter.RepositoryAdapterListener, RepositoryContract.View {

    private RepositoryAdapter adapter;
    private EndlessRecyclerOnScrollListener scrollListener;
    private RepositoryContract.Presenter presenter;
    private ProgressBar progressBar;
    private ProgressBar progressBarRecycler;

    private TextView messageDisplay;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);

        setupToolbar();
        setupRecyclerView();
        setFindViewById();

        initialize(savedInstanceState);
    }

    private void setFindViewById() {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        messageDisplay = (TextView) findViewById(R.id.tv_message_display);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.container);
        progressBarRecycler = (ProgressBar) findViewById(R.id.progress_bar_recycler);
    }

    private void setupToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    private void initialize(Bundle savedInstanceState) {
        presenter = new RepositoryPresenter(this,
                Injection.provideTasksRepository(getApplicationContext()),
                SchedulerProvider.getInstance());

        if (savedInstanceState != null) {
            adapter.onRestoreInstanceState(savedInstanceState);
            scrollListener.onRestoreInstanceState(savedInstanceState);
        }

        if (!adapter.hasItems()) {
            scrollListener.setCurrentPage(1);
            presenter.loadRepositories();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        adapter.onSaveInstanceState(outState);
        scrollListener.onSaveInstanceState(outState);
    }

    private void setupRecyclerView() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RepositoryAdapter(this);
        recyclerView.setAdapter(adapter);

        scrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                presenter.setCurrentPage(currentPage);
                presenter.loadRepositories();
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void onClickListItem(Repository repository) {
        presenter.openPullRequest(repository);
    }

    @Override
    public void showRepositories(List<Repository> repositories) {
        adapter.addData(repositories);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressList() {
        progressBarRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressList() {
        progressBarRecycler.setVisibility(View.GONE);
    }

    @Override
    public void showPullRequestUi(Repository repository) {
        PullRequestActivity.navigate(this, repository.name, repository.getLoginUser());
    }

    @Override
    public void showSnackbarNoConnection() {
        Snackbar.make(coordinatorLayout, R.string.title_no_connection, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.title_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.loadRepositories();
                    }
                }).show();
    }

    @Override
    public void showSnackbarError() {
        Snackbar.make(coordinatorLayout, R.string.error_message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.title_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.loadRepositories();
                    }
                }).show();
    }

    @Override
    public void showNoRepositoriesMessage() {
        messageDisplay.setText(R.string.no_movies_message);
        messageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoRepositoriesMessage() {
        messageDisplay.setVisibility(View.GONE);
    }

    @Override
    public boolean isNetworkAvailable() {
        return Utility.isNetworkAvailable(this);
    }
}
