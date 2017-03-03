package com.thales.github.data.remote;

import com.thales.github.model.pullrequest.PullRequest;
import com.thales.github.model.repository.Repositories;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface GitHubApi {

    @GET("search/repositories")
    Observable<Repositories> getRepositories(@Query("q") String language, @Query("sort") String sort,
                                             @Query("page") int page);

    @GET("repos/{user}/{repository}/pulls")
    Observable<List<PullRequest>> getPullRequests(@Path("user") String user, @Path("repository") String repository);

}
