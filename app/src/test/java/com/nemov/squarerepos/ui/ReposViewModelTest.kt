package com.nemov.squarerepos.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nemov.squarerepos.repository.RepoRepository
import com.nemov.squarerepos.util.mock
import com.nemov.squarerepos.vo.Repo
import com.nemov.squarerepos.vo.Resource
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class ReposViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repoRepository = mock(RepoRepository::class.java)
    private val reposViewModel = ReposViewModel(repoRepository)

    @Test
    fun loadRepositories() {
        reposViewModel.repositories.observeForever(mock())
        verifyNoMoreInteractions(repoRepository)
        reposViewModel.setLogin("foo")
        verify(repoRepository).loadRepos("foo")
        reset(repoRepository)
        reposViewModel.setLogin("bar")
        verify(repoRepository).loadRepos("bar")
    }

    @Test
    fun retry() {
        reposViewModel.setLogin("foo")
        verifyNoMoreInteractions(repoRepository)
        reposViewModel.retry()
        verifyNoMoreInteractions(repoRepository)
        val repoObserver = mock<Observer<Resource<List<Repo>>>>()
        reposViewModel.repositories.observeForever(repoObserver)

        verify(repoRepository).loadRepos("foo")
        reset(repoRepository)

        reposViewModel.retry()
        verify(repoRepository).loadRepos("foo")
        reposViewModel.repositories.removeObserver(repoObserver)

        reposViewModel.retry()
        verifyNoMoreInteractions(repoRepository)
    }

    @Test
    fun nullRepoList() {
        val observer = mock<Observer<Resource<List<Repo>>>>()
        reposViewModel.setLogin("foo")
        reposViewModel.setLogin(null)
        reposViewModel.repositories.observeForever(observer)
        verify(observer).onChanged(null)
    }

    @Test
    fun dontRefreshOnSameData() {
        val observer = mock<Observer<String?>>()
        reposViewModel.organization.observeForever(observer)
        verifyNoMoreInteractions(observer)
        reposViewModel.setLogin("foo")
        verify(observer).onChanged("foo")
        reset(observer)
        reposViewModel.setLogin("foo")
        verifyNoMoreInteractions(observer)
        reposViewModel.setLogin("bar")
        verify(observer).onChanged("bar")
    }

    @Test
    fun noRetryWithoutOrganization() {
        reposViewModel.retry()
        verifyNoMoreInteractions(repoRepository)
    }
}