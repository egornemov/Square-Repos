package com.nemov.squarerepos.repository

import androidx.lifecycle.LiveData
import com.nemov.squarerepos.AppExecutors
import com.nemov.squarerepos.api.GithubService
import com.nemov.squarerepos.db.RepoDao
import com.nemov.squarerepos.util.RateLimiter
import com.nemov.squarerepos.vo.Repo
import com.nemov.squarerepos.vo.Resource
import java.util.concurrent.TimeUnit
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
class RepoRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val repoDao: RepoDao,
    private val githubService: GithubService
) {

    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun loadRepos(organization: String): LiveData<Resource<List<Repo>>> {
        return object : NetworkBoundResource<List<Repo>, List<Repo>>(appExecutors) {
            override fun saveCallResult(item: List<Repo>) {
                repoDao.insertRepos(item)
            }

            override fun shouldFetch(data: List<Repo>?) =
                data == null || data.isEmpty() || repoListRateLimit.shouldFetch(organization)

            override fun loadFromDb() = repoDao.loadRepositories()

            override fun createCall() = githubService.getRepositories(organization)

            override fun onFetchFailed() {
                repoListRateLimit.reset(organization)
            }
        }.asLiveData()
    }
}