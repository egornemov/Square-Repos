package com.nemov.squarerepos.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nemov.squarerepos.api.GithubService
import com.nemov.squarerepos.db.GithubDb
import com.nemov.squarerepos.db.RepoDao
import com.nemov.squarerepos.util.ApiUtil.successCall
import com.nemov.squarerepos.util.InstantAppExecutors
import com.nemov.squarerepos.util.mock
import com.nemov.squarerepos.vo.Repo
import com.nemov.squarerepos.vo.Resource
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

@RunWith(JUnit4::class)
class RepoRepositoryTest {
    private lateinit var repository: RepoRepository
    private val dao = mock(RepoDao::class.java)
    private val service = mock(GithubService::class.java)
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        val db = mock(GithubDb::class.java)
        `when`(db.repoDao()).thenReturn(dao)
        `when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        repository = RepoRepository(InstantAppExecutors(), dao, service)
    }

    @Test
    fun loadReposFromNetwork() {
        val dbData = MutableLiveData<List<Repo>>()
        `when`(dao.loadRepositories()).thenReturn(dbData)

        val repos = listOf(Repo("foo", "bar", 42))
        val call = successCall(repos)
        `when`(service.getRepositories("square")).thenReturn(call)

        val data = repository.loadRepos("square")
        verify(dao).loadRepositories()
        verifyNoMoreInteractions(service)

        val observer = mock<Observer<Resource<List<Repo>>>>()
        data.observeForever(observer)
        verifyNoMoreInteractions(service)
        verify(observer).onChanged(Resource.loading(null))
        val updatedDbData = MutableLiveData<List<Repo>>()
        `when`(dao.loadRepositories()).thenReturn(updatedDbData)

        dbData.postValue(null)
        verify(service).getRepositories("square")
        verify(dao).insertRepos(repos)

        updatedDbData.postValue(repos)
        verify(observer).onChanged(Resource.success(repos))
    }
}