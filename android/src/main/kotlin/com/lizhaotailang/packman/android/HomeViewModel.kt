package com.lizhaotailang.packman.android

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.lizhaotailang.packman.android.jobs.JobsDataSource
import com.lizhaotailang.packman.common.data.History
import com.lizhaotailang.packman.common.data.Pipeline
import com.lizhaotailang.packman.common.data.PipelineScheduleListItem
import com.lizhaotailang.packman.common.data.toRealmInstant
import com.lizhaotailang.packman.common.database.PackmanDatabase
import com.lizhaotailang.packman.common.database.realmFilePath
import com.lizhaotailang.packman.common.network.ApolloGraphQLClient
import com.lizhaotailang.packman.common.network.Resource
import com.lizhaotailang.packman.common.network.Status
import com.lizhaotailang.packman.common.network.api.GitLabApi
import com.lizhaotailang.packman.common.ui.new.Variant
import io.ktor.client.call.body
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val gitLabApi = GitLabApi()

    private val _requestFlow: MutableStateFlow<Resource<Pipeline>?> =
        MutableStateFlow(value = null)
    val requestFlow: StateFlow<Resource<Pipeline>?> = _requestFlow

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    val database = PackmanDatabase(
        scope = viewModelScope,
        realmFilePath = realmFilePath(getApplication())
    )

    val jobsFlow by lazy {
        Pager(
            config = defaultPagingConfig,
            pagingSourceFactory = {
                JobsDataSource(
                    apolloClient = ApolloGraphQLClient.CLIENT.apolloClient
                )
            }
        ).flow.cachedIn(scope = viewModelScope)
    }

    private val _allPipelineSchedulesFlow =
        MutableStateFlow<Resource<List<PipelineScheduleListItem>>>(value = Resource.loading(data = null))
    val allPipelineSchedulesFlow: StateFlow<Resource<List<PipelineScheduleListItem>>> get() = _allPipelineSchedulesFlow

    init {
        getAllPipelineSchedules(init = true)
    }

    fun showSnackbarMessage(newMessage: String) {
        viewModelScope.launch {
            try {
                _snackbarMessage.emit(newMessage)
            } catch (e: Exception) {
                Log.e(TAG, null, e)
            }
        }
    }

    fun triggerNewPipeline(
        branch: String,
        variants: List<Variant>
    ) {
        if (_requestFlow.value?.status == Status.LOADING) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _requestFlow.emit(Resource.loading(data = null))

                val pipeline: Pipeline = gitLabApi.triggerPipeline(
                    branch = branch,
                    variants = variants
                ).body()

                _requestFlow.emit(Resource.success(data = pipeline))

                database.realm.write {
                    this.copyToRealm(
                        History().apply {
                            this.branch = branch
                            this.variants = variants.map { it.ordinal }.toRealmList()
                            this.pipelineId = pipeline.id
                            this.startedAt = pipeline.createdAt.toRealmInstant()
                        }
                    )
                }

                showSnackbarMessage(newMessage = "Success")
            } catch (e: Exception) {
                _requestFlow.emit(Resource.failed(exception = e, data = null))

                Log.e(TAG, null, e)

                showSnackbarMessage("Failed to run pipeline")
            }
        }
    }

    fun deleteHistory(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                database.realm.write {
                    val h = this.query<History>("_id == $0", history._id)
                        .find()
                        .first()
                    delete(h)
                }
            } catch (e: Exception) {
                Log.e(TAG, null, e)
            }
        }
    }

    fun getAllPipelineSchedules(init: Boolean = false) {
        if (_allPipelineSchedulesFlow.value.status == Status.LOADING
            && !init
        ) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _allPipelineSchedulesFlow.emit(value = Resource.loading(data = null))

                val pipelineSchedules = gitLabApi.getAllPipelineSchedules()
                    .body<List<PipelineScheduleListItem>>()

                _allPipelineSchedulesFlow.emit(value = Resource.success(data = pipelineSchedules))
            } catch (e: Exception) {
                _allPipelineSchedulesFlow.emit(value = Resource.failed(exception = e, data = null))

                Log.e(TAG, null, e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        database.realm.close()
    }

    companion object {

        const val KEY_HOME_SCREEN = "key_home_screen"

        private const val TAG = "HomeViewModel"

    }

}
