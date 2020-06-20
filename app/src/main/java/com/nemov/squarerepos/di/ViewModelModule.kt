package com.nemov.squarerepos.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.nemov.squarerepos.ui.ReposViewModel
import com.nemov.squarerepos.viewmodel.GithubViewModelFactory

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ReposViewModel::class)
    abstract fun bindReposViewModel(reposViewModel: ReposViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: GithubViewModelFactory): ViewModelProvider.Factory
}
