package com.thales.github.repositories;


import com.thales.github.data.repository.GitHubRepository;
import com.thales.github.model.repository.Owner;
import com.thales.github.model.repository.Repositories;
import com.thales.github.model.repository.Repository;
import com.thales.github.ui.repository.RepositoryContract;
import com.thales.github.ui.repository.RepositoryPresenter;
import com.thales.github.utilities.schedulers.BaseSchedulerProvider;
import com.thales.github.utilities.schedulers.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RepositoryPresenterTest {
    private static final List<Repository> REPOSITORIES = new ArrayList<>();

    @Mock
    private GitHubRepository tasksRepository;

    @Mock
    private RepositoryContract.View tasksView;

    private RepositoryPresenter tasksPresenter;

    @Before
    public void setupRepositoryPresenter() {
        MockitoAnnotations.initMocks(this);
        BaseSchedulerProvider schedulerProvider = new ImmediateSchedulerProvider();

        tasksPresenter = new RepositoryPresenter(tasksView, tasksRepository, schedulerProvider);

        for (int i = 0; i < 30; i++) {
            Repository repository = new Repository();
            repository.id = i;
            repository.name = "name " + i;
            repository.description = "description" + i;
            repository.starsCount = i;
            repository.forksCount = i;
            repository.owner = new Owner();
            repository.owner.avatarUrl = "https://avatars.githubusercontent.com/u/6407041?v=3";
            repository.owner.login = "login" + i;
            repository.owner.id = i;

            REPOSITORIES.add(repository);
        }
    }

    @Test
    public void loadRepositoriesFromRepositoryAndLoadIntoView() {
        Repositories repo = new Repositories();
        repo.repositories = REPOSITORIES;

        when(tasksRepository.getRepositories("language:Java", "stars", 1)).thenReturn(Observable.just(repo));

        tasksPresenter.loadRepositories();
        verify(tasksView).showProgress();
        verify(tasksView).hideProgress();

        verify(tasksView).showRepositories(anyList());
    }

    @Test
    public void loadNoRepositoriesFromRepository_ShowsMessage() {
        Repositories repo = new Repositories();
        repo.repositories = new ArrayList<>();

        when(tasksRepository.getRepositories("language:Java", "stars", 1)).thenReturn(Observable.just(repo));

        tasksPresenter.loadRepositories();
        verify(tasksView).showProgress();
        verify(tasksView).hideProgress();

        verify(tasksView).showNoRepositoriesMessage();
    }

    @Test
    public void errorLoadingRepository_ShowsError() {
        when(tasksView.isNetworkAvailable()).thenReturn(true);
        when(tasksRepository.getRepositories("language:Java", "stars", 1))
                .thenReturn(Observable.<Repositories>error(new Exception()));

        tasksPresenter.loadRepositories();
        verify(tasksView).showProgress();
        verify(tasksView).hideProgress();

        verify(tasksView).showSnackbarError();
    }

    @Test
    public void errorLoadingRepositoryNoConnection_ShowsError() {
        when(tasksView.isNetworkAvailable()).thenReturn(false);
        when(tasksRepository.getRepositories("language:Java", "stars", 1))
                .thenReturn(Observable.<Repositories>error(new Exception()));

        tasksPresenter.loadRepositories();
        verify(tasksView).showProgress();
        verify(tasksView).hideProgress();

        verify(tasksView).showSnackbarNoConnection();
    }

    @Test
    public void clickOnRepository_ShowsPullRequestUi() {
        tasksPresenter.openPullRequest(REPOSITORIES.get(0));

        verify(tasksView).showPullRequestUi(any(Repository.class));
    }
}
