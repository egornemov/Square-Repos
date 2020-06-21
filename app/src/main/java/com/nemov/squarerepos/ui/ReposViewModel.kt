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
    private val _organization = MutableLiveData<String?>()
    val organization: LiveData<String?>
        get() = _organization
    val repositories: LiveData<Resource<List<Repo>>> = _organization.switchMap {
        if (it == null) {
            AbsentLiveData.create()
        } else {
            repoRepository.loadRepos(it)
        }
    }

    fun setLogin(login: String?) {
        if (_organization.value != login) {
            _organization.value = login
        }
    }

    fun retry() {
        _organization.value?.let {
            _organization.value = it
        }
    }
}
