package com.lizhaotailang.packman.common.ui

sealed class Screen(val route: String) {

    object HomeScreen : Screen(route = "home")

    object DebugScreen : Screen(route = "debug")

    object JobScreen :
        Screen(route = "job/{$ARG_JOB_ID}?$ARG_CANCELABLE={$ARG_CANCELABLE}&$ARG_RETRYABLE={$ARG_RETRYABLE}&$ARG_TRIGGERED={$ARG_TRIGGERED}")

    companion object {

        const val ARG_JOB_ID = "arg_job_id"
        const val ARG_RETRYABLE = "arg_retryable"
        const val ARG_CANCELABLE = "arg_cancelable"
        const val ARG_TRIGGERED = "arg_triggered"

    }

}
