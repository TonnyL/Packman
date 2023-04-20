package com.lizhaotailang.packman.common.ui.job

import com.lizhaotailang.packman.common.IOScheduler
import com.lizhaotailang.packman.common.data.History
import com.lizhaotailang.packman.common.data.Job
import com.lizhaotailang.packman.common.database.PackmanDatabase
import com.lizhaotailang.packman.common.network.Resource
import com.lizhaotailang.packman.common.network.Status
import com.lizhaotailang.packman.common.network.api.GitLabApi
import com.lizhaotailang.packman.common.ui.new.Variant
import io.ktor.client.call.body
import io.realm.kotlin.ext.query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalStdlibApi::class)
class JobViewModelDelegate(
    private val scope: CoroutineScope,
    private val database: PackmanDatabase,
    private val jobId: String,
    private val cancelable: Boolean,
    private val retryable: Boolean
) : JobViewModelFeatures {

    private val _retryFlow = MutableStateFlow<Status?>(value = null)
    override val retryStatusFlow: StateFlow<Status?> get() = _retryFlow

    private val _cancelFlow = MutableStateFlow<Status?>(value = null)
    override val cancelStatusFlow: StateFlow<Status?> get() = _cancelFlow

    private val _jobResourceFlow =
        MutableStateFlow<Resource<Job>>(value = Resource.loading(data = null))
    override val jobResourceFlow: StateFlow<Resource<Job>> get() = _jobResourceFlow

    private val _variantsFlow = MutableStateFlow<String?>(value = null)
    override val variantsFlow: StateFlow<String?> get() = _variantsFlow

    private val _snackbarMessageFlow = MutableStateFlow("")
    override val snackbarMessageFlow: StateFlow<String> get() = _snackbarMessageFlow

    private val api = GitLabApi()

    override fun fetchJobInfo() {
        scope.launch(IOScheduler) {
            try {
                _jobResourceFlow.emit(value = Resource.loading(data = null))

                val job = api.getASingleJob(jobId = jobId).body<Job>()

                val history = database.realm.query<History>("pipelineId = $0", job.pipeline.id)
                    .first()
                    .find()

                _variantsFlow.emit(
                    value = history?.variants?.joinToString {
                        Variant.entries[it].variant
                    }
                )

                _jobResourceFlow.emit(value = Resource.success(data = job))
            } catch (e: Exception) {
                e.printStackTrace()

                _jobResourceFlow.emit(value = Resource.failed(exception = e, data = null))
            }
        }
    }

    override fun retry() {
        if (!retryable
            || retryStatusFlow.value == Status.LOADING
        ) {
            return
        }

        scope.launch(IOScheduler) {
            try {
                _retryFlow.emit(value = Status.LOADING)

                api.retryAJob(jobId = jobId)

                _retryFlow.emit(value = Status.SUCCEEDED)

                _snackbarMessageFlow.emit("Retried")
            } catch (e: Exception) {
                e.printStackTrace()

                _retryFlow.emit(value = Status.FAILED)

                _snackbarMessageFlow.emit("Failed to retry")
            }
        }
    }

    override fun cancel() {
        if (!cancelable
            || cancelStatusFlow.value == Status.LOADING
        ) {
            return
        }

        scope.launch(IOScheduler) {
            try {
                _cancelFlow.emit(value = Status.LOADING)

                api.cancelAJob(jobId = jobId)

                _cancelFlow.emit(value = Status.SUCCEEDED)

                _snackbarMessageFlow.emit("Cancelled")
            } catch (e: Exception) {
                e.printStackTrace()

                _cancelFlow.emit(value = Status.FAILED)

                _snackbarMessageFlow.emit("Failed to cancel")
            }
        }
    }

    override fun clearSnackbarMessage() {
        scope.launch {
            try {
                _snackbarMessageFlow.emit("")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
