package com.lizhaotailang.packman.common.ui

sealed class Screen(val route: String) {

    object HomeScreen : Screen(route = "home")

    object DebugScreen : Screen(route = "debug")

    data class JobScreen(
        val jobId: String,
        val cancelable: Boolean,
        val retryable: Boolean,
        val triggered: Boolean
    ) : Screen(route = ROUTE) {

        companion object {
            const val ROUTE =
                "job/{$ARG_JOB_ID}?$ARG_CANCELABLE={$ARG_CANCELABLE}&$ARG_RETRYABLE={$ARG_RETRYABLE}&$ARG_TRIGGERED={$ARG_TRIGGERED}"
        }

    }

    data class ScheduleScreen(
        val pipelineScheduleId: Int
    ) : Screen(route = ROUTE) {

        companion object {
            const val ROUTE = "szchedule/{$ARG_PIPELINE_SCHEDULE_ID}"
        }

    }

    companion object {

        const val ARG_JOB_ID = "arg_job_id"
        const val ARG_RETRYABLE = "arg_retryable"
        const val ARG_CANCELABLE = "arg_cancelable"
        const val ARG_TRIGGERED = "arg_triggered"

        const val ARG_PIPELINE_SCHEDULE_ID = "arg_pipeline_schedule_id"

    }

}
