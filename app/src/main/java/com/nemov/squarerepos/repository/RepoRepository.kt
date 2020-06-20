package com.nemov.squarerepos.repository

import androidx.lifecycle.LiveData
import com.nemov.squarerepos.AppExecutors
import com.nemov.squarerepos.api.GithubService
import com.nemov.squarerepos.db.RepoDao
import com.nemov.squarerepos.testing.OpenForTesting
import com.nemov.squarerepos.vo.Repo
import com.nemov.squarerepos.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles Repo instances.
 *
 * unfortunate naming :/ .
 * Repo - value object name
 * Repository - type of this class.
 */
@Singleton
@OpenForTesting
class RepoRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val repoDao: RepoDao,
    private val githubService: GithubService
) {

    fun loadRepos(owner: String): LiveData<Resource<List<Repo>>> {
        return object : NetworkBoundResource<List<Repo>, List<Repo>>(appExecutors) {
            override fun saveCallResult(item: List<Repo>) {
                repoDao.insertRepos(item)
            }

            override fun shouldFetch(data: List<Repo>?) =
                data == null || data.isEmpty()

            override fun loadFromDb() = repoDao.loadRepositories()

            override fun createCall() = githubService.getRepositories(owner)
        }.asLiveData()
    }
}