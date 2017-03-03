package com.thales.github.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.thales.github.data.GitHubDataSource;
import com.thales.github.data.remote.GitHubApi;
import com.thales.github.model.pullrequest.PullRequest;
import com.thales.github.model.repository.Repositories;

import java.util.List;

import rx.Observable;

public class GitHubRepository implements GitHubDataSource {
    @Nullable
    private static GitHubRepository instance = null;

    @NonNull
    private final GitHubApi gitHubApi;

    private GitHubRepository(@NonNull GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    public static GitHubRepository getInstance(@NonNull GitHubApi gitHubApi) {
        if (instance == null) {
            instance = new GitHubRepository(gitHubApi);
        }
        return instance;
    }

    public Observable<Repositories> getRepositories(String language, String sort, int page) {
        return gitHubApi.getRepositories(language, sort, page);
    }

    public Observable<List<PullRequest>> getPullRequests(String user, String repository) {
        return gitHubApi.getPullRequests(user, repository);
    }
}
