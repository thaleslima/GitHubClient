package com.thales.github.ui.repository;

import android.support.annotation.NonNull;

import com.thales.github.data.repository.GitHubRepository;
import com.thales.github.model.repository.Repositories;
import com.thales.github.model.repository.Repository;
import com.thales.github.utilities.schedulers.BaseSchedulerProvider;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class RepositoryPresenter implements RepositoryContract.Presenter {
    private static final String LANGUAGE = "language:Java";
    private static final String SORT = "stars";

    @NonNull
    private final GitHubRepository repository;

    @NonNull
    private final CompositeSubscription subscriptions;

    @NonNull
    private final RepositoryContract.View view;

    @NonNull
    private final BaseSchedulerProvider schedulerProvider;

    private int currentPage = 1;

    public RepositoryPresenter(@NonNull RepositoryContract.View view,
                               @NonNull GitHubRepository gitHubRepository,
                               @NonNull BaseSchedulerProvider schedulerProvider) {
        this.repository = gitHubRepository;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public void openPullRequest(Repository repository) {
        view.showPullRequestUi(repository);
    }

    @Override
    public void loadRepositories() {
        if (currentPage > 1) {
            view.showProgressList();
        } else {
            view.showProgress();
        }

        view.hideNoRepositoriesMessage();

        Observable<Repositories> repositories = repository.getRepositories(LANGUAGE, SORT, currentPage);
        Subscription subscription = repositories
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(new Subscriber<Repositories>() {
                    @Override
                    public void onCompleted() {
                        view.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //Log.e(TAG, e.getMessage());

                        view.hideProgressList();
                        view.hideProgress();

                        if (!view.isNetworkAvailable()) {
                            view.showSnackbarNoConnection();
                        } else {
                            view.showSnackbarError();
                        }
                    }

                    @Override
                    public void onNext(Repositories repositories) {
                        view.hideProgressList();

                        if (repositories.repositories != null && repositories.repositories.size() > 0) {
                            view.showRepositories(repositories.repositories);
                        } else {
                            view.showNoRepositoriesMessage();
                        }
                    }
                });

        subscriptions.add(subscription);
    }


    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }
}
