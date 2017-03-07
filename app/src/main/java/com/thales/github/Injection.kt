package com.thales.github

import android.content.Context

import com.thales.github.data.remote.GitHubClient
import com.thales.github.data.repository.GitHubRepository
import com.thales.github.utilities.schedulers.SchedulerProvider

import com.google.gson.internal.`$Gson$Preconditions`.checkNotNull

object Injection {
    fun provideTasksRepository(context: Context): GitHubRepository {
        checkNotNull(context)
        return GitHubRepository.getInstance(GitHubClient.getClient(context)!!)
    }
}
