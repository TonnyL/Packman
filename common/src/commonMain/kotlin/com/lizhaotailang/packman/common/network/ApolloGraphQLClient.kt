package com.lizhaotailang.packman.common.network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.adapter.KotlinxInstantAdapter
import com.apollographql.apollo3.api.http.HttpHeader
import com.apollographql.apollo3.network.http.HttpNetworkTransport
import com.apollographql.apollo3.network.http.LoggingInterceptor
import com.lizhaotailang.packman.common.CommonBuildConfig
import com.lizhaotailang.packman.graphql.type.JobID
import com.lizhaotailang.packman.graphql.type.Time

class ApolloGraphQLClient(accessToken: String) {

    val apolloClient: ApolloClient by lazy {
        ApolloClient.Builder()
            .networkTransport(
                networkTransport = HttpNetworkTransport.Builder()
                    .serverUrl(serverUrl = CommonBuildConfig.GRAPH_QL_SERVER_URL)
                    .interceptors(interceptors = listOf(LoggingInterceptor()))
                    .httpHeaders(
                        headers = listOf(
                            HttpHeader("Accept", "application/json"),
                            HttpHeader("Content-Type", "application/json"),
                            HttpHeader("Authorization", "Bearer $accessToken")
                        )
                    )
                    .build()
            )
            .addCustomScalarAdapter(
                customScalarType = Time.type,
                customScalarAdapter = KotlinxInstantAdapter
            )
            .addCustomScalarAdapter(
                customScalarType = JobID.type,
                customScalarAdapter = JobIdAdapter
            )
            .build()
    }

    companion object {

        val CLIENT = ApolloGraphQLClient(accessToken = CommonBuildConfig.ACCESS_TOKEN)

    }

}
