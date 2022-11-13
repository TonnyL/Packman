package com.lizhaotailang.packman.common.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.lizhaotailang.packman.common.ui.MainScreenNavigationItem.Jobs
import com.lizhaotailang.packman.common.ui.MainScreenNavigationItem.New

enum class MainScreenNavigationItem(val item: String) {

    Jobs("Jobs"),

    New("New")

}

val MainScreenNavigationItem.icon: ImageVector
    get() = when (this) {
        Jobs -> {
            Icons.Outlined.List
        }
        New -> {
            Icons.Outlined.Edit
        }
    }
