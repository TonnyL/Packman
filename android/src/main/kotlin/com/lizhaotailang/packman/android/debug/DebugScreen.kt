package com.lizhaotailang.packman.android.debug

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lizhaotailang.packman.android.LocalNavController
import com.lizhaotailang.packman.android.PackmanTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugScreen() {
    val navController = LocalNavController.current
    Scaffold(
        topBar = {
            PackmanTopBar(
                title = {
                    Text(text = "Debug")
                },
                navigationIcon = {
                    IconButton(onClick = navController::navigateUp) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        DebugScreenContent(paddingValues = paddingValues)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DebugScreenContent(paddingValues: PaddingValues) {
    val context = LocalContext.current

    val viewModel = viewModel(
        key = DebugViewModel.KEY_DEBUG,
        initializer = {
            DebugViewModel(app = context.applicationContext as Application)
        }
    )

    val histories by viewModel.realmDatabase.historiesFlow.collectAsStateWithLifecycle()

    LazyColumn(contentPadding = paddingValues) {
        item(key = "insert_history") {
            ListItem(
                headlineText = {
                    Text(text = "Insert history into database")
                },
                supportingText = {
                    Text(text = "size: ${histories.size}")
                },
                modifier = Modifier.clickable(onClick = viewModel::insertHistoryIntoDatabase)
            )
        }
    }
}
