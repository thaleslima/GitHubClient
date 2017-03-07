package com.thales.github.ui.pullresquest

import android.util.Log

import com.thales.github.data.repository.GitHubRepository
import com.thales.github.model.pullrequest.PullRequest

import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

internal class PullRequestPresenter(private val view: PullRequestContract.View,
                                    private val repository: GitHubRepository) : PullRequestContract.Presenter {

    private val subscriptions: CompositeSubscription = CompositeSubscription()

    override fun loadPullRequests(title: String, username: String) {
        view.showProgress()
        view.hideNoRepositoriesMessage()

        val repositories = repository.getPullRequests(username, title)
        val subscription = repositories
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<List<PullRequest>>() {
                    override fun onCompleted() {
                        view.hideProgress()
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, e.message)

                        view.hideProgress()

                        if (!view.isNetworkAvailable()) {
                            view.showSnackbarNoConnection()
                        } else {
                            view.showSnackbarError()
                        }
                    }

                    override fun onNext(pullRequests: List<PullRequest>) {
                        if (pullRequests.isNotEmpty()) {
                            view.showPullRequests(pullRequests)
                        } else {
                            view.showNoRepositoriesMessage()
                        }
                    }
                })

        subscriptions.add(subscription)
    }

    override fun unsubscribe() {
        this.subscriptions.clear()
    }

    companion object {
        private val TAG = PullRequestPresenter::class.java.name
    }
}
