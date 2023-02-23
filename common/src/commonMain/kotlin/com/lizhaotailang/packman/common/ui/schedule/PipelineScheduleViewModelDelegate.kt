package com.lizhaotailang.packman.common.ui.schedule

import com.lizhaotailang.packman.common.data.PipelineScheduleDetails
import com.lizhaotailang.packman.common.network.Resource
import com.lizhaotailang.packman.common.network.Status
import com.lizhaotailang.packman.common.network.api.GitLabApi
import io.ktor.client.call.body
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PipelineScheduleViewModelDelegate(
    private val scope: CoroutineScope,
    private val pipelineScheduleId: Int
) : PipelineScheduleViewModelFeatures {

    private val api = GitLabApi()

    private val _getASinglePipelineScheduleFlow =
        MutableStateFlow<Resource<PipelineScheduleDetails>>(value = Resource.loading(data = null))
    override val getASinglePipelineScheduleFlow: StateFlow<Resource<PipelineScheduleDetails>>
        get() = _getASinglePipelineScheduleFlow

    override fun getASinglePipelineSchedule(init: Boolean) {
        if (getASinglePipelineScheduleFlow.value.status == Status.LOADING
            && !init
        ) {
            return
        }

        scope.launch {
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

                e.printStackTrace()
            }
        }
    }

}
