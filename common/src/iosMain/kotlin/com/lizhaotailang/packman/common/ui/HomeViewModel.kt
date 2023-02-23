package com.lizhaotailang.packman.common.ui

import com.lizhaotailang.packman.common.IOScheduler
import com.lizhaotailang.packman.common.data.History
import com.lizhaotailang.packman.common.data.Pipeline
import com.lizhaotailang.packman.common.data.PipelineScheduleListItem
import com.lizhaotailang.packman.common.database.PackmanDatabase
import com.lizhaotailang.packman.common.network.ApolloGraphQLClient
import com.lizhaotailang.packman.common.network.Resource
import com.lizhaotailang.packman.common.ui.new.Variant
import com.lizhaotailang.packman.graphql.AllJobsQuery
import com.lizhaotailang.packman.graphql.fragment.CiJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import platform.Foundation.NSHomeDirectory

class HomeViewModel : HomeViewModelFeatures {

    private val viewModelScope = CoroutineScope(Dispatchers.Main) + Dispatchers.Main.immediate

    val database = PackmanDatabase(
        scope = viewModelScope,
        realmFilePath = "${NSHomeDirectory()}/Documents/packman.realm"
    )

    private val delegate = HomeViewModelDelegate(
        scope = viewModelScope,
        database = database
    )

    private val _jobsFlow =
        MutableStateFlow<Resource<List<CiJob>>>(value = Resource.loading(data = null))
    val jobsFlow: StateFlow<Resource<List<CiJob>>?>
        get() = _jobsFlow

    override val allPipelineSchedulesFlow: StateFlow<Resource<List<PipelineScheduleListItem>>>
        get() = delegate.allPipelineSchedulesFlow

    override val requestFlow: StateFlow<Resource<Pipeline>?>
        get() = delegate.requestFlow

    override val snackbarMessage: StateFlow<String>
        get() = delegate.snackbarMessage

    init {
        refreshJobs()
        getAllPipelineSchedules(init = true)
    }

    override fun getAllPipelineSchedules(init: Boolean) {
        delegate.getAllPipelineSchedules(init = init)
    }

    override fun showSnackbarMessage(newMessage: String) {
        delegate.showSnackbarMessage(newMessage = newMessage)
    }

    override fun triggerNewPipeline(branch: String, variants: List<Variant>) {
        delegate.triggerNewPipeline(
            branch = branch,
            variants = variants
        )
    }

    override fun deleteHistory(history: History) {
        delegate.deleteHistory(history = history)
    }

    fun refreshJobs() {
        viewModelScope.launch(IOScheduler) {
            try {
                val project = ApolloGraphQLClient.CLIENT.apolloClient.query(
                    query = AllJobsQuery(
                        projectPath = "android/project",
                        perPage = PER_PAGE
                    )
                ).execute().data?.project

                _jobsFlow.emit(
                    value = Resource.success(
                        data = project?.allJobs?.nodes.orEmpty().mapNotNull {
                            it?.ciJob
                        }
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()

                _jobsFlow.emit(value = Resource.failed(exception = e, data = null))
            }
        }
    }

    companion object {

        const val KEY = "HomeViewModel"

    }

}
