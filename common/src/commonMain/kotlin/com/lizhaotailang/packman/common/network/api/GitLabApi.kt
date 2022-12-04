package com.lizhaotailang.packman.common.network.api

import com.lizhaotailang.packman.common.CommonBuildConfig
import com.lizhaotailang.packman.common.network.KtorClient
import com.lizhaotailang.packman.common.ui.new.Variant
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLBuilder

class GitLabApi {

    suspend fun triggerPipeline(
        branch: String,
        variants: List<Variant>
    ): HttpResponse {
        return KtorClient.httpClient.post(url = URLBuilder(urlString = "$BASE_URL/projects/12/trigger/pipeline").build()) {
            formData {
                parameter("token", CommonBuildConfig.TRIGGER_PIPELINE_ACCESS_TOKEN)
                parameter("ref", branch)
                parameter("variables[BUILD_VARIANT]", variants.joinToString())
            }
        }
    }

    suspend fun getASingleJob(jobId: String): HttpResponse {
        return KtorClient.httpClient.get(url = URLBuilder(urlString = "$BASE_URL/projects/12/jobs/$jobId").build()) {
            header("PRIVATE-TOKEN", CommonBuildConfig.ACCESS_TOKEN)
        }
    }

    suspend fun cancelAJob(jobId: String): HttpResponse {
        return KtorClient.httpClient.post(url = URLBuilder(urlString = "$BASE_URL/projects/12/jobs/$jobId/cancel").build()) {
            header("PRIVATE-TOKEN", CommonBuildConfig.ACCESS_TOKEN)
        }
    }

    suspend fun retryAJob(jobId: String): HttpResponse {
        return KtorClient.httpClient.post(url = URLBuilder(urlString = "$BASE_URL/projects/12/jobs/$jobId/retry").build()) {
            header("PRIVATE-TOKEN", CommonBuildConfig.ACCESS_TOKEN)
        }
    }

    companion object {

        private const val BASE_URL = "https://gitlab.insta360.com/api/v4"

    }

}
