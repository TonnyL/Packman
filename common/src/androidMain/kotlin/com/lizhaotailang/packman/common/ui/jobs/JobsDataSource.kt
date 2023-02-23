package com.lizhaotailang.packman.common.ui.jobs

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.lizhaotailang.packman.common.CommonBuildConfig
import com.lizhaotailang.packman.common.IOScheduler
import com.lizhaotailang.packman.common.ext.checkedEndCursor
import com.lizhaotailang.packman.common.ext.checkedStartCursor
import com.lizhaotailang.packman.graphql.AllJobsQuery
import com.lizhaotailang.packman.graphql.fragment.CiJob
import kotlinx.coroutines.withContext

class JobsDataSource(
    private val apolloClient: ApolloClient
) : PagingSource<String, CiJob>() {

    override fun getRefreshKey(state: PagingState<String, CiJob>): String? = null

    override val keyReuseSupported: Boolean = true

    override suspend fun load(params: LoadParams<String>): LoadResult<String, CiJob> {
        val list = mutableListOf<CiJob>()

        return withContext(IOScheduler) {
            try {
                val project = apolloClient.query(
                    query = AllJobsQuery(
                        projectPath = CommonBuildConfig.PROJECT_PATH,
                        perPage = params.loadSize,
                        before = Optional.presentIfNotNull(params.key),
                        after = Optional.presentIfNotNull(params.key)
                    )
                ).execute().data?.project

                list.addAll(
                    project?.allJobs?.nodes.orEmpty().mapNotNull {
                        it?.ciJob
                    }
                )

                val pageInfo = project?.allJobs?.pageInfo?.pageInfo

                LoadResult.Page(
                    data = list,
                    prevKey = pageInfo.checkedStartCursor,
                    nextKey = pageInfo.checkedEndCursor
                )
            } catch (e: Exception) {
                Log.e("JobsDataSource", null, e)

                LoadResult.Error(e)
            }
        }
    }

}
