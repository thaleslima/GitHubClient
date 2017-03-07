package com.thales.github.data.remote

import com.thales.github.model.pullrequest.PullRequest
import com.thales.github.model.repository.Repositories

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

interface GitHubApi {

    @GET("search/repositories")
    fun getRepositories(@Query("q") language: String, @Query("sort") sort: String,
                        @Query("page") page: Int): Observable<Repositories>

    @GET("repos/{user}/{repository}/pulls")
    fun getPullRequests(@Path("user") user: String, @Path("repository") repository: String): Observable<List<PullRequest>>

}
