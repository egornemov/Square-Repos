package com.nemov.squarerepos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.nemov.squarerepos.AppExecutors
import com.nemov.squarerepos.R
import com.nemov.squarerepos.databinding.ReposFragmentBinding
import com.nemov.squarerepos.di.Injectable
import com.nemov.squarerepos.ui.common.RepoListAdapter
import com.nemov.squarerepos.ui.common.RetryCallback
import com.nemov.squarerepos.util.autoCleared
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReposFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appExecutors: AppExecutors

    var binding by autoCleared<ReposFragmentBinding>()

    private val reposViewModel: ReposViewModel by viewModels {
        viewModelFactory
    }
    private val params by navArgs<ReposFragmentArgs>()
    private var adapter by autoCleared<RepoListAdapter>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<ReposFragmentBinding>(
            inflater,
            R.layout.repos_fragment,
            container,
            false
        )
        dataBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                reposViewModel.retry()
            }
        }
        binding = dataBinding
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.move)
        // Make sure we don't wait longer than a second for the image request
        postponeEnterTransition(1, TimeUnit.SECONDS)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        reposViewModel.setLogin(params.login)
        binding.repos = reposViewModel.repositories
        binding.lifecycleOwner = viewLifecycleOwner
        val rvAdapter = RepoListAdapter(appExecutors)
        binding.repoList.adapter = rvAdapter
        this.adapter = rvAdapter
        initRepoList()
    }

    private fun initRepoList() {
        reposViewModel.repositories.observe(viewLifecycleOwner, Observer { repos ->
            adapter.submitList(repos?.data)
        })
    }
}
