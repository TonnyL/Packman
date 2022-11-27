package com.lizhaotailang.packman.common.ui

sealed class Screen(val route: String) {

    object HomeScreen : Screen(route = "home")

    object DebugScreen : Screen(route = "debug")

}
