package com.lizhaotailang.packman.common.ui.schedule

import com.lizhaotailang.packman.common.data.PipelineScheduleDetails
import com.lizhaotailang.packman.common.network.Resource
import kotlinx.coroutines.flow.StateFlow

interface PipelineScheduleViewModelFeatures {

    val getASinglePipelineScheduleFlow: StateFlow<Resource<PipelineScheduleDetails>>

    fun getASinglePipelineSchedule(init: Boolean = false)

}
