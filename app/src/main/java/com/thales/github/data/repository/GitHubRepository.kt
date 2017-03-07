package com.thales.github.data.repository

import com.thales.github.data.GitHubDataSource
import com.thales.github.data.remote.GitHubApi
import com.thales.github.model.pullrequest.PullRequest
import com.thales.github.model.repository.Repositories

import rx.Observable

class GitHubRepository private constructor(private val gitHubApi: GitHubApi) : GitHubDataSource {

    override fun getRepositories(language: String, sort: String, page: Int): Observable<Repositories> {
        return gitHubApi.getRepositories(language, sort, page)
    }

    override fun getPullRequests(user: String, repository: String): Observable<List<PullRequest>> {
        return gitHubApi.getPullRequests(user, repository)
    }

    companion object {
        private var instance: GitHubRepository? = null

        fun getInstance(gitHubApi: GitHubApi): GitHubRepository {
            if (instance == null) {
                instance = GitHubRepository(gitHubApi)
            }
            return instance as GitHubRepository
        }
    }
}
