package com.nemov.squarerepos.repository

import androidx.lifecycle.LiveData
import com.nemov.squarerepos.AppExecutors
import com.nemov.squarerepos.api.GithubService
import com.nemov.squarerepos.db.UserDao
import com.nemov.squarerepos.testing.OpenForTesting
import com.nemov.squarerepos.vo.Resource
import com.nemov.squarerepos.vo.User
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles User objects.
 */
@OpenForTesting
@Singleton
class UserRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val userDao: UserDao,
    private val githubService: GithubService
) {

    fun loadUser(login: String): LiveData<Resource<User>> {
        return object : NetworkBoundResource<User, User>(appExecutors) {
            override fun saveCallResult(item: User) {
                userDao.insert(item)
            }

            override fun shouldFetch(data: User?) = data == null

            override fun loadFromDb() = userDao.findByLogin(login)

            override fun createCall() = githubService.getUser(login)
        }.asLiveData()
    }
}
