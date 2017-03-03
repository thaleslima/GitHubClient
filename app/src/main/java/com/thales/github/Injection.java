package com.thales.github;

import android.content.Context;
import android.support.annotation.NonNull;

import com.thales.github.data.remote.GitHubClient;
import com.thales.github.data.repository.GitHubRepository;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class Injection {
    public static GitHubRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);
        return GitHubRepository.getInstance(GitHubClient.getClient(context));
    }
}
