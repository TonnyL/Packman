package com.lizhaotailang.packman.common.ui.schedule

import com.lizhaotailang.packman.common.data.PipelineScheduleDetails
import com.lizhaotailang.packman.common.network.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.plus

class PipelineScheduleViewModel(
    pipelineScheduleId: Int
) : PipelineScheduleViewModelFeatures {

    private val viewModelScope = CoroutineScope(Dispatchers.Main) + Dispatchers.Main.immediate

    private val delegate = PipelineScheduleViewModelDelegate(
        scope = viewModelScope,
        pipelineScheduleId = pipelineScheduleId
    )

    override val getASinglePipelineScheduleFlow: StateFlow<Resource<PipelineScheduleDetails>>
        get() = delegate.getASinglePipelineScheduleFlow

    init {
        delegate.getASinglePipelineSchedule(init = true)
    }

    override fun getASinglePipelineSchedule(init: Boolean) {
        delegate.getASinglePipelineSchedule(init = init)
    }
}
