package com.lizhaotailang.packman.common.ui

import com.lizhaotailang.packman.common.IOScheduler
import com.lizhaotailang.packman.common.data.History
import com.lizhaotailang.packman.common.data.Pipeline
import com.lizhaotailang.packman.common.data.PipelineScheduleListItem
import com.lizhaotailang.packman.common.data.toRealmInstant
import com.lizhaotailang.packman.common.database.PackmanDatabase
import com.lizhaotailang.packman.common.network.Resource
import com.lizhaotailang.packman.common.network.Status
import com.lizhaotailang.packman.common.network.api.GitLabApi
import com.lizhaotailang.packman.common.ui.new.Variant
import io.ktor.client.call.body
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModelDelegate(
    private val scope: CoroutineScope,
    private val database: PackmanDatabase
) {

    private val gitLabApi = GitLabApi()

    private val _allPipelineSchedulesFlow =
        MutableStateFlow<Resource<List<PipelineScheduleListItem>>>(value = Resource.loading(data = null))
    val allPipelineSchedulesFlow: StateFlow<Resource<List<PipelineScheduleListItem>>> get() = _allPipelineSchedulesFlow

    private val _requestFlow: MutableStateFlow<Resource<Pipeline>?> =
        MutableStateFlow(value = null)
    val requestFlow: StateFlow<Resource<Pipeline>?> = _requestFlow

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: StateFlow<String> = _snackbarMessage

    fun getAllPipelineSchedules(init: Boolean = false) {
        if (_allPipelineSchedulesFlow.value.status == Status.LOADING
            && !init
        ) {
            return
        }

        scope.launch(IOScheduler) {
            try {
                _allPipelineSchedulesFlow.emit(value = Resource.loading(data = null))

                val pipelineSchedules = gitLabApi.getAllPipelineSchedules()
                    .body<List<PipelineScheduleListItem>>()

                _allPipelineSchedulesFlow.emit(value = Resource.success(data = pipelineSchedules))
            } catch (e: Exception) {
                _allPipelineSchedulesFlow.emit(value = Resource.failed(exception = e, data = null))

                e.printStackTrace()
            }
        }
    }

    fun showSnackbarMessage(newMessage: String) {
        scope.launch {
            try {
                _snackbarMessage.emit(newMessage)
            } catch (e: Exception) {
                e.printStackTrace()
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

        scope.launch(IOScheduler) {
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

                e.printStackTrace()

                showSnackbarMessage("Failed to run pipeline")
            }
        }
    }

    fun deleteHistory(history: History) {
        scope.launch(IOScheduler) {
            try {
                database.realm.write {
                    val h = this.query<History>("_id == $0", history._id)
                        .find()
                        .first()
                    delete(h)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
