package com.lizhaotailang.packman.common.ui.job

import com.lizhaotailang.packman.common.data.Job
import com.lizhaotailang.packman.common.database.PackmanDatabase
import com.lizhaotailang.packman.common.network.Resource
import com.lizhaotailang.packman.common.network.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.plus
import platform.Foundation.NSHomeDirectory

class JobViewModel(
    jobId: String,
    cancelable: Boolean,
    retryable: Boolean
) : JobViewModelFeatures {

    private val viewModelScope = CoroutineScope(Dispatchers.Main) + Dispatchers.Main.immediate

    private val database = PackmanDatabase(
        scope = viewModelScope,
        realmFilePath = "${NSHomeDirectory()}/Documents/packman.realm"
    )

    private val delegate = JobViewModelDelegate(
        scope = viewModelScope,
        database = database,
        jobId = jobId,
        cancelable = cancelable,
        retryable = retryable
    )

    override val retryStatusFlow: StateFlow<Status?>
        get() = delegate.retryStatusFlow

    override val cancelStatusFlow: StateFlow<Status?>
        get() = delegate.cancelStatusFlow

    override val jobResourceFlow: StateFlow<Resource<Job>>
        get() = delegate.jobResourceFlow

    override val snackbarMessageFlow: StateFlow<String>
        get() = delegate.snackbarMessageFlow

    override val variantsFlow: StateFlow<String?>
        get() = delegate.snackbarMessageFlow

    init {
        fetchJobInfo()
    }

    override fun fetchJobInfo() {
        delegate.fetchJobInfo()
    }

    override fun retry() {
        delegate.retry()
    }

    override fun cancel() {
        delegate.cancel()
    }

    override fun clearSnackbarMessage() {
        delegate.clearSnackbarMessage()
    }

}
