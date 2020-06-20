package com.nemov.squarerepos.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.nemov.squarerepos.repository.RepoRepository
import com.nemov.squarerepos.testing.OpenForTesting
import com.nemov.squarerepos.util.AbsentLiveData
import com.nemov.squarerepos.vo.Repo
import com.nemov.squarerepos.vo.Resource
import javax.inject.Inject

@OpenForTesting
class ReposViewModel @Inject constructor(repoRepository: RepoRepository) : ViewModel() {
    private val organization = MutableLiveData<String?>()
    val repositories: LiveData<Resource<List<Repo>>> = organization.switchMap { login ->
        if (login == null) {
            AbsentLiveData.create()
        } else {
            repoRepository.loadRepos(login)
        }
    }

    fun setLogin(login: String?) {
        if (organization.value != login) {
            organization.value = login
        }
    }

    fun retry() {
        organization.value?.let {
            organization.value = it
        }
    }
}
