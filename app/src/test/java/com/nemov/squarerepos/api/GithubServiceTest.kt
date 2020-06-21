package com.nemov.squarerepos.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nemov.squarerepos.util.LiveDataCallAdapterFactory
import com.nemov.squarerepos.util.getOrAwaitValue
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

        assertThat(repos.size, `is`(30))

        val repo = repos[0]
        assertThat(repo.name, `is`("yajl-objc"))
        assertThat(repo.description, `is`("Objective-C bindings for YAJL (Yet Another JSON Library) C library"))
        assertThat(repo.stars, `is`(8))

        val repo2 = repos[1]
        assertThat(repo2.name, `is`("simplerrd"))
        assertThat(repo2.description, `is`("SimpleRRD provides a simple Ruby interface for creating graphs with RRD"))
        assertThat(repo2.stars, `is`(20))
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
