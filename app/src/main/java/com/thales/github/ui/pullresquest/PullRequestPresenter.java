package com.thales.github.ui.pullresquest;

import android.support.annotation.NonNull;
import android.util.Log;

import com.thales.github.data.repository.GitHubRepository;
import com.thales.github.model.pullrequest.PullRequest;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

class PullRequestPresenter implements PullRequestContract.Presenter {
    private static final String TAG = PullRequestPresenter.class.getName();

    @NonNull
    private final GitHubRepository repository;

    @NonNull
    private final CompositeSubscription subscriptions;

    @NonNull
    private final PullRequestContract.View view;

    PullRequestPresenter(@NonNull PullRequestContract.View view,
                         @NonNull GitHubRepository gitHubRepository) {
        this.view = view;
        this.repository = gitHubRepository;
        this.subscriptions = new CompositeSubscription();
    }

    @Override
    public void loadPullRequests(String title, String username) {
        view.showProgress();
        view.hideNoRepositoriesMessage();

        Observable<List<PullRequest>> repositories = repository.getPullRequests(username, title);
        Subscription subscription = repositories
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<PullRequest>>() {
                    @Override
                    public void onCompleted() {
                        view.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());

                        view.hideProgress();

                        if (!view.isNetworkAvailable()) {
                            view.showSnackbarNoConnection();
                        } else {
                            view.showSnackbarError();
                        }
                    }

                    @Override
                    public void onNext(List<PullRequest> pullRequests) {
                        if (pullRequests.size() > 0) {
                            view.showPullRequests(pullRequests);
                        } else {
                            view.showNoRepositoriesMessage();
                        }
                    }
                });

        subscriptions.add(subscription);
    }

    @Override
    public void unsubscribe() {
        this.subscriptions.clear();
    }
}
