package com.lizhaotailang.packman.common.ui

import com.lizhaotailang.packman.common.data.History
import com.lizhaotailang.packman.common.data.Pipeline
import com.lizhaotailang.packman.common.data.PipelineScheduleListItem
import com.lizhaotailang.packman.common.network.Resource
import com.lizhaotailang.packman.common.ui.new.Variant
import com.lizhaotailang.packman.graphql.fragment.CiJob
import kotlinx.coroutines.flow.StateFlow

interface HomeViewModelFeatures {

    val allPipelineSchedulesFlow: StateFlow<Resource<List<PipelineScheduleListItem>>>

    val requestFlow: StateFlow<Resource<Pipeline>?>

    val snackbarMessage: StateFlow<String>

    fun getAllPipelineSchedules(init: Boolean = false)

    fun showSnackbarMessage(newMessage: String)

    fun triggerNewPipeline(
        branch: String,
        variants: List<Variant>
    )

    fun deleteHistory(history: History)

}
