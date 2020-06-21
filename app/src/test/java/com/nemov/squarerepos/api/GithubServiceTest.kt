package com.nemov.squarerepos.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nemov.squarerepos.util.LiveDataCallAdapterFactory
import com.nemov.squarerepos.util.getOrAwaitValue
import com.nemov.squarerepos.vo.Repo
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class GithubServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: GithubService

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(GithubService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getRepos() {
        enqueueResponse("repos-square.json")
        val repos = (service.getRepositories("square").getOrAwaitValue() as ApiSuccessResponse).body

        val request = mockWebServer.takeRequest()
        assertThat(request.path, `is`("/orgs/square/repos"))

        assertThat(repos.size, `is`(3))

        val data = listOf(
            Repo(
                "yajl-objc",
                "Objective-C bindings for YAJL (Yet Another JSON Library) C library",
                8
            ),
            Repo(
                "simplerrd",
                "SimpleRRD provides a simple Ruby interface for creating graphs with RRD",
                20
            ),
            Repo(
                "gh-unit",
                "Test Framework for Objective-C",
                18
            )
        )

        data.forEachIndexed { index, target ->
            val repo = repos[index]
            assertThat(repo, `is`(target))
        }
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream("api-response/$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }
}
