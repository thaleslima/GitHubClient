package com.thales.github.ui.pullresquest

import com.thales.github.model.pullrequest.PullRequest
import com.thales.github.ui.BasePresenter

internal interface PullRequestContract {
    interface View {
        fun showPullRequests(pullRequests: List<PullRequest>)

        fun showProgress()

        fun hideProgress()

        fun showSnackbarNoConnection()

        fun showSnackbarError()

        fun showNoRepositoriesMessage()

        fun hideNoRepositoriesMessage()

        fun isNetworkAvailable(): Boolean
    }

    interface Presenter : BasePresenter {
        fun loadPullRequests(title: String, username: String)
    }
}
