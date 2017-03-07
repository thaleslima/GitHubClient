package com.thales.github.data

import com.thales.github.model.pullrequest.PullRequest
import com.thales.github.model.repository.Repositories

import rx.Observable

interface GitHubDataSource {
    fun getRepositories(language: String, sort: String, page: Int): Observable<Repositories>

    fun getPullRequests(user: String, repository: String): Observable<List<PullRequest>>
}
