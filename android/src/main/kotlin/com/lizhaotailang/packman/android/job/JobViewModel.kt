package com.lizhaotailang.packman.android.job

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lizhaotailang.packman.common.data.History
import com.lizhaotailang.packman.common.data.Job
import com.lizhaotailang.packman.common.database.PackmanDatabase
import com.lizhaotailang.packman.common.database.realmFilePath
import com.lizhaotailang.packman.common.network.Resource
import com.lizhaotailang.packman.common.network.Status
import com.lizhaotailang.packman.common.network.api.GitLabApi
import com.lizhaotailang.packman.common.ui.new.Variant
import io.ktor.client.call.body
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JobViewModel(
    private val jobId: String,
    private val cancelable: Boolean,
    private val retryable: Boolean,
    app: Application
) : AndroidViewModel(app) {

    private val realmDatabase = PackmanDatabase(
        scope = viewModelScope,
        realmFilePath = realmFilePath(getApplication())
    )

    private val _retryFlow = MutableStateFlow<Status?>(value = null)
    val retryStatusFlow: StateFlow<Status?> get() = _retryFlow

    private val _cancelFlow = MutableStateFlow<Status?>(value = null)
    val cancelStatusFlow: StateFlow<Status?> get() = _cancelFlow

    private val _jobResourceFlow =
        MutableStateFlow<Resource<Job>>(value = Resource.loading(data = null))
    val jobResourceFlow: StateFlow<Resource<Job>> get() = _jobResourceFlow

    private val _variantsFlow = MutableStateFlow<String?>(value = null)
    val variantsFlow: StateFlow<String?> get() = _variantsFlow

    private val _snackbarMessageFlow = MutableStateFlow("")
    val snackbarMessageFlow: StateFlow<String> get() = _snackbarMessageFlow

    private val api = GitLabApi()

    init {
        fetchJobInfo()
    }

    fun fetchJobInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _jobResourceFlow.emit(value = Resource.loading(data = null))

                val job = api.getASingleJob(jobId = jobId).body<Job>()

                val history = realmDatabase.realm.query<History>("pipelineId = $0", job.pipeline.id)
                    .first()
                    .find()

                _variantsFlow.emit(
                    value = history?.variants?.joinToString {
                        Variant.values()[it].variant
                    }
                )

                _jobResourceFlow.emit(value = Resource.success(data = job))
            } catch (e: Exception) {
                Log.e(TAG, null, e)

                _jobResourceFlow.emit(value = Resource.failed(exception = e, data = null))
            }
        }
    }

    fun retry() {
        if (!retryable
            || retryStatusFlow.value == Status.LOADING
        ) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _retryFlow.emit(value = Status.LOADING)

                api.retryAJob(jobId = jobId)

                _retryFlow.emit(value = Status.SUCCEEDED)

                _snackbarMessageFlow.emit("Retried")
            } catch (e: Exception) {
                Log.e(TAG, null, e)

                _retryFlow.emit(value = Status.FAILED)

                _snackbarMessageFlow.emit("Failed to retry")
            }
        }
    }

    fun cancel() {
        if (!cancelable
            || cancelStatusFlow.value == Status.LOADING
        ) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _cancelFlow.emit(value = Status.LOADING)

                api.cancelAJob(jobId = jobId)

                _cancelFlow.emit(value = Status.SUCCEEDED)

                _snackbarMessageFlow.emit("Cancelled")
            } catch (e: Exception) {
                Log.e(TAG, null, e)

                _cancelFlow.emit(value = Status.FAILED)

                _snackbarMessageFlow.emit("Failed to cancel")
            }
        }
    }

    fun clearSnackbarMessage() {
        viewModelScope.launch {
            try {
                _snackbarMessageFlow.emit("")
            } catch (e: Exception) {
                Log.e(TAG, null, e)
            }
        }
    }

    companion object {

        private const val TAG = "JobViewModel"

    }

}
