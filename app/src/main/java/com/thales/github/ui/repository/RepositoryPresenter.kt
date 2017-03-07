package com.thales.github.ui.repository

import com.thales.github.data.repository.GitHubRepository
import com.thales.github.model.repository.Repositories
import com.thales.github.model.repository.Repository
import com.thales.github.utilities.schedulers.BaseSchedulerProvider

import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.subscriptions.CompositeSubscription

class RepositoryPresenter(private val view: RepositoryContract.View,
                          private val repository: GitHubRepository,
                          private val schedulerProvider: BaseSchedulerProvider) : RepositoryContract.Presenter {

    private val subscriptions: CompositeSubscription

    private var currentPage = 1

    init {
        this.subscriptions = CompositeSubscription()
    }

    override fun setCurrentPage(currentPage: Int) {
        this.currentPage = currentPage
    }

    override fun openPullRequest(repository: Repository) {
        view.showPullRequestUi(repository)
    }

    override fun loadRepositories() {
        if (currentPage > 1) {
            view.showProgressList()
        } else {
            view.showProgress()
        }

        view.hideNoRepositoriesMessage()

        val repositories = repository.getRepositories(LANGUAGE, SORT, currentPage)
        val subscription = repositories
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        { repositories ->
                            view.hideProgress()
                            view.hideProgressList()

                            if (repositories.repositories?.size!! > 0) {
                                view.showRepositories(repositories.repositories!!)
                            } else {
                                view.showNoRepositoriesMessage()
                            }
                        },
                        { e ->
                            view.hideProgressList()
                            view.hideProgress()

                            if (!view.isNetworkAvailable()) {
                                view.showSnackbarNoConnection()
                            } else {
                                view.showSnackbarError()
                            }
                        }
                )

        subscriptions.add(subscription)
    }


    override fun unsubscribe() {
        subscriptions.clear()
    }

    companion object {
        private val LANGUAGE = "language:Java"
        private val SORT = "stars"
    }
}
