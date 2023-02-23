package com.lizhaotailang.packman.common.ui.job

import com.lizhaotailang.packman.common.data.Job
import com.lizhaotailang.packman.common.network.Resource
import com.lizhaotailang.packman.common.network.Status
import kotlinx.coroutines.flow.StateFlow

interface JobViewModelFeatures {

    val retryStatusFlow: StateFlow<Status?>

    val cancelStatusFlow: StateFlow<Status?>

    val jobResourceFlow: StateFlow<Resource<Job>>

    val snackbarMessageFlow: StateFlow<String>

    val variantsFlow: StateFlow<String?>

    fun fetchJobInfo()

    fun retry()

    fun cancel()

    fun clearSnackbarMessage()

}
