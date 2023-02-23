package com.lizhaotailang.packman.common.ui.debug

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import com.lizhaotailang.packman.common.data.History
import com.lizhaotailang.packman.common.ui.components.PackmanTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DebugScreenContent(
    histories: List<History>,
    insertHistoryIntoDatabase: () -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            PackmanTopBar(
                title = {
                    Text(text = "Debug")
                },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            item(key = "insert_history") {
                ListItem(
                    headlineText = {
                        Text(text = "Insert history into database")
                    },
                    supportingText = {
                        Text(text = "size: ${histories.size}")
                    },
                    modifier = Modifier.clickable(onClick = insertHistoryIntoDatabase)
                )
            }
        }
    }

}
