package com.lizhaotailang.packman.common.network.api

import com.lizhaotailang.packman.common.CommonBuildConfig
import com.lizhaotailang.packman.common.network.KtorClient
import com.lizhaotailang.packman.common.ui.new.Variant
import io.ktor.client.request.forms.formData
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLBuilder

class GitLabApi {

    suspend fun triggerPipeline(
        branch: String,
        variants: List<Variant>
    ): HttpResponse {
        return KtorClient.httpClient.post(url = URLBuilder(urlString = "${CommonBuildConfig.REST_SERVER_URL}/projects/${CommonBuildConfig.PROJECT_ID}/trigger/pipeline").build()) {
            formData {
                parameter("token", CommonBuildConfig.TRIGGER_PIPELINE_ACCESS_TOKEN)
                parameter("ref", branch)
                parameter("variables[BUILD_VARIANT]", variants.joinToString())
            }
        }
    }

}
