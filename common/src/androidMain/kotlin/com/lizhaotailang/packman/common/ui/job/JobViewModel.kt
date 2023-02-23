package com.lizhaotailang.packman.common.ui.job

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lizhaotailang.packman.common.data.Job
import com.lizhaotailang.packman.common.database.PackmanDatabase
import com.lizhaotailang.packman.common.database.realmFilePath
import com.lizhaotailang.packman.common.network.Resource
import com.lizhaotailang.packman.common.network.Status
import kotlinx.coroutines.flow.StateFlow

class JobViewModel(
    jobId: String,
    cancelable: Boolean,
    retryable: Boolean,
    app: Application
) : AndroidViewModel(app), JobViewModelFeatures {

    private val realmDatabase = PackmanDatabase(
        scope = viewModelScope,
        realmFilePath = realmFilePath(getApplication())
    )

    private val delegate = JobViewModelDelegate(
        scope = viewModelScope,
        database = realmDatabase,
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
        get() = delegate.variantsFlow

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
