package com.nemov.squarerepos.api

import androidx.lifecycle.LiveData
import com.nemov.squarerepos.vo.Repo
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * REST API access points
 */
interface GithubService {
    @GET("orgs/{organization}/repos")
    fun getRepositories(
        @Path("organization") organization: String
    ): LiveData<ApiResponse<List<Repo>>>
}
