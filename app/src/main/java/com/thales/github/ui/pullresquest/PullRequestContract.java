package com.thales.github.ui.pullresquest;

import com.thales.github.model.pullrequest.PullRequest;
import com.thales.github.ui.BasePresenter;

import java.util.List;

interface PullRequestContract {
    interface View {
        void showPullRequests(List<PullRequest> pullRequests);

        void showProgress();

        void hideProgress();

        void showSnackbarNoConnection();

        void showSnackbarError();

        void showNoRepositoriesMessage();

        void hideNoRepositoriesMessage();

        boolean isNetworkAvailable();
    }

    interface Presenter extends BasePresenter {
        void loadPullRequests(String title, String username);
    }
}
