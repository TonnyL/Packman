package com.lizhaotailang.packman.common.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lizhaotailang.packman.common.data.PipelineScheduleDetails
import com.lizhaotailang.packman.common.network.Resource
import kotlinx.coroutines.flow.StateFlow

class PipelineScheduleViewModel(
    pipelineScheduleId: Int
) : ViewModel(), PipelineScheduleViewModelFeatures {

    private val delegate = PipelineScheduleViewModelDelegate(
        scope = viewModelScope,
        pipelineScheduleId = pipelineScheduleId
    )

    override val getASinglePipelineScheduleFlow: StateFlow<Resource<PipelineScheduleDetails>>
        get() = delegate.getASinglePipelineScheduleFlow

    init {
        getASinglePipelineSchedule(init = true)
    }

    override fun getASinglePipelineSchedule(init: Boolean) {
        delegate.getASinglePipelineSchedule(init = init)
    }

}
