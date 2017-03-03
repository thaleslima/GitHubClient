package com.thales.github.ui.pullresquest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thales.github.Injection;
import com.thales.github.R;
import com.thales.github.model.pullrequest.PullRequest;
import com.thales.github.utilities.Utility;

import java.util.List;

public class PullRequestActivity extends AppCompatActivity
        implements PullRequestAdapter.RepositoryAdapterListener, PullRequestContract.View {

    private static final String EXTRA_TITLE = "title";
    private static final String EXTRA_USERNAME = "username";

    private ProgressBar progressBar;
    private TextView messageDisplay;
    private CoordinatorLayout coordinatorLayout;

    private PullRequestContract.Presenter presenter;
    private PullRequestAdapter adapter;
    private String title;
    private String username;

    public static void navigate(Activity activity, String title, String username) {
        Intent it = new Intent(activity, PullRequestActivity.class);
        it.putExtra(EXTRA_TITLE, title);
        it.putExtra(EXTRA_USERNAME, username);

        activity.startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_request);

        initExtras();
        setupToolbar();
        setupRecyclerView();
        setFindViewById();

        initialize(savedInstanceState);
    }

    private void initialize(Bundle savedInstanceState) {
        presenter = new PullRequestPresenter(this, Injection.provideTasksRepository(getApplicationContext()));

        if (savedInstanceState != null) {
            adapter.onRestoreInstanceState(savedInstanceState);
        }

        if (!adapter.hasItems()) {
            presenter.loadPullRequests(title, username);
        }
    }

    private void initExtras() {
        title = getIntent().getStringExtra(EXTRA_TITLE);
        username = getIntent().getStringExtra(EXTRA_USERNAME);
    }

    private void setupToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }
    }

    private void setFindViewById() {
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        messageDisplay = (TextView) findViewById(R.id.tv_message_display);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.container);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        adapter.onSaveInstanceState(outState);
    }

    private void setupRecyclerView() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PullRequestAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickListItem(PullRequest pullRequest) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(pullRequest.htmlUrl));
        startActivity(i);
    }

    @Override
    public void showPullRequests(List<PullRequest> pullRequests) {
        adapter.replaceData(pullRequests);
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
    public void showSnackbarNoConnection() {
        Snackbar.make(coordinatorLayout, R.string.title_no_connection, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.title_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.loadPullRequests(title, username);
                    }
                }).show();
    }

    @Override
    public void showSnackbarError() {
        Snackbar.make(coordinatorLayout, R.string.error_message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.title_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.loadPullRequests(title, username);
                    }
                }).show();
    }

    @Override
    public void showNoRepositoriesMessage() {
        messageDisplay.setText(R.string.no_pull_request_message);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
