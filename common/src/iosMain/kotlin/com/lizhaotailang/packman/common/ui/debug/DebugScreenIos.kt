package com.lizhaotailang.packman.common.ui.debug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.lizhaotailang.packman.common.BottomPadding
import com.lizhaotailang.packman.common.TopPadding
import com.lizhaotailang.packman.common.ui.PackmanTheme
import com.lizhaotailang.packman.common.ui.Screen

@Composable
internal fun DebugScreen(
    insets: WindowInsets,
    currentScreenState: MutableState<Screen>
) {
    PackmanTheme {
        val viewModel = remember { DebugViewModel() }

        val histories by viewModel.database.historiesFlow.collectAsState()

        Column {
            TopPadding(insets = insets)

            DebugScreenContent(
                histories = histories,
                insertHistoryIntoDatabase = viewModel::insertHistoryIntoDatabase,
                navigateUp = {
                    currentScreenState.value = Screen.HomeScreen
                }
            )

            BottomPadding(insets = insets)
        }
    }
}
