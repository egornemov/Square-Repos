package com.nemov.squarerepos.api

import androidx.lifecycle.LiveData
import com.nemov.squarerepos.vo.Repo
import com.nemov.squarerepos.vo.User
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * REST API access points
 */
interface GithubService {
    @GET("users/{login}")
    fun getUser(@Path("login") login: String): LiveData<ApiResponse<User>>

    @GET("orgs/{organization}/repos")
    fun getOrganizationRepos(
        @Path("organization") organization: String
    ): LiveData<ApiResponse<List<Repo>>>
}
