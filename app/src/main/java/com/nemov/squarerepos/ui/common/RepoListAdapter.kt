package com.nemov.squarerepos.ui.common

import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import com.nemov.squarerepos.AppExecutors
import com.nemov.squarerepos.R
import com.nemov.squarerepos.databinding.RepoItemBinding
import com.nemov.squarerepos.vo.Repo

/**
 * A RecyclerView adapter for [Repo] class.
 */
class RepoListAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors
) : DataBoundListAdapter<Repo, RepoItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Repo>() {
        override fun areItemsTheSame(oldItem: Repo, newItem: Repo) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Repo, newItem: Repo) =
            oldItem.description == newItem.description
                    && oldItem.stars == newItem.stars
    }
) {

    override fun createBinding(parent: ViewGroup): RepoItemBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.repo_item,
            parent,
            false,
            dataBindingComponent
        )

    override fun bind(binding: RepoItemBinding, item: Repo) {
        binding.repo = item
    }
}
