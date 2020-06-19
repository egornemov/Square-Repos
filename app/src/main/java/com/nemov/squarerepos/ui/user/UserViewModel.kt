package com.nemov.squarerepos.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.nemov.squarerepos.repository.RepoRepository
import com.nemov.squarerepos.repository.UserRepository
import com.nemov.squarerepos.testing.OpenForTesting
import com.nemov.squarerepos.util.AbsentLiveData
import com.nemov.squarerepos.vo.Repo
import com.nemov.squarerepos.vo.Resource
import com.nemov.squarerepos.vo.User
import javax.inject.Inject

@OpenForTesting
class UserViewModel
@Inject constructor(
    userRepository: UserRepository,
    repoRepository: RepoRepository) : ViewModel() {
    private val _login = MutableLiveData<String?>()
    val login: LiveData<String?>
        get() = _login
    val repositories: LiveData<Resource<List<Repo>>> = _login.switchMap { login ->
        if (login == null) {
            AbsentLiveData.create()
        } else {
            repoRepository.loadRepos(login)
        }
    }
    val user: LiveData<Resource<User>> = _login.switchMap { login ->
        if (login == null) {
            AbsentLiveData.create()
        } else {
            userRepository.loadUser(login)
        }
    }

    fun setLogin(login: String?) {
        if (_login.value != login) {
            _login.value = login
        }
    }

    fun retry() {
        _login.value?.let {
            _login.value = it
        }
    }
}
