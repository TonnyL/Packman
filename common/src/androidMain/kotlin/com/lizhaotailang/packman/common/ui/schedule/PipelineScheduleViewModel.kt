package com.lizhaotailang.packman.common.ui.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lizhaotailang.packman.common.data.PipelineScheduleDetails
import com.lizhaotailang.packman.common.network.Resource
import com.lizhaotailang.packman.common.network.Status
import com.lizhaotailang.packman.common.network.api.GitLabApi
import io.ktor.client.call.body
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PipelineScheduleViewModel(
    private val pipelineScheduleId: Int
) : ViewModel() {

    private val api = GitLabApi()

    private val _getASinglePipelineScheduleFlow =
        MutableStateFlow<Resource<PipelineScheduleDetails>>(value = Resource.loading(data = null))
    val getASinglePipelineScheduleFlow: StateFlow<Resource<PipelineScheduleDetails>>
        get() = _getASinglePipelineScheduleFlow

    init {
        getASinglePipelineSchedule(init = true)
    }

    fun getASinglePipelineSchedule(init: Boolean = false) {
        if (getASinglePipelineScheduleFlow.value.status == Status.LOADING
            && !init
        ) {
            return
        }

        viewModelScope.launch {
            try {
                _getASinglePipelineScheduleFlow.emit(value = Resource.loading(data = null))

                val details =
                    api.getASinglePipelineSchedule(pipelineScheduleId = pipelineScheduleId)
                        .body<PipelineScheduleDetails>()

                _getASinglePipelineScheduleFlow.emit(value = Resource.success(data = details))
            } catch (e: Exception) {
                _getASinglePipelineScheduleFlow.emit(
                    value = Resource.failed(
                        exception = e,
                        data = null
                    )
                )

                Log.e(TAG, null, e)
            }
        }
    }

    companion object {

        private const val TAG = "PipelineSchedulesViewModel"

    }

}
