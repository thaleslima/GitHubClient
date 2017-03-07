package com.thales.github.ui.repository

import com.thales.github.model.repository.Repository
import com.thales.github.ui.BasePresenter

interface RepositoryContract {
    interface View {
        fun showRepositories(repositories: List<Repository>)

        fun showProgress()

        fun hideProgress()

        fun showSnackbarNoConnection()

        fun showSnackbarError()

        fun showNoRepositoriesMessage()

        fun hideNoRepositoriesMessage()

        fun isNetworkAvailable(): Boolean

        fun showProgressList()

        fun hideProgressList()

        fun showPullRequestUi(repository: Repository)
    }

    interface Presenter : BasePresenter {
        fun loadRepositories()

        fun setCurrentPage(currentPage: Int)

        fun openPullRequest(repository: Repository)
    }
}
