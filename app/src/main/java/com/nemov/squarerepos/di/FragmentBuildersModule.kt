package com.nemov.squarerepos.di

import com.nemov.squarerepos.ui.ReposFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeUserFragment(): ReposFragment
}
