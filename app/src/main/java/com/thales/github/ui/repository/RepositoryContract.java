package com.thales.github.ui.repository;

import com.thales.github.model.repository.Repository;
import com.thales.github.ui.BasePresenter;

import java.util.List;

public interface RepositoryContract {
    interface View {
        void showRepositories(List<Repository> repositories);

        void showProgress();

        void hideProgress();

        void showSnackbarNoConnection();

        void showSnackbarError();

        void showNoRepositoriesMessage();

        void hideNoRepositoriesMessage();

        boolean isNetworkAvailable();

        void showProgressList();

        void hideProgressList();

        void showPullRequestUi(Repository repository);
    }

    interface Presenter extends BasePresenter {
        void loadRepositories();

        void setCurrentPage(int currentPage);

        void openPullRequest(Repository repository);
    }
}
