package com.thales.github.data;

import com.thales.github.model.pullrequest.PullRequest;
import com.thales.github.model.repository.Repositories;

import java.util.List;

import rx.Observable;

public interface GitHubDataSource {
    Observable<Repositories> getRepositories(String language, String sort, int page);

    Observable<List<PullRequest>> getPullRequests(String user, String repository);
}
