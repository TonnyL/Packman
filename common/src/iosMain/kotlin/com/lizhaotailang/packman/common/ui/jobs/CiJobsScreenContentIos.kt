package com.lizhaotailang.packman.common.ui.jobs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lizhaotailang.packman.graphql.fragment.CiJob

@Composable
internal fun CiJobsScreenContent(
    innerPaddings: PaddingValues,
    jobs: List<CiJob>,
    navigateToJobDetails: (CiJob) -> Unit
) {
    LazyColumn {
        itemsIndexed(
            items = jobs,
            key = { _, item ->
                item.id ?: ""
            }
        ) { index, item ->
            if (index == 0) {
                Spacer(modifier = Modifier.height(height = innerPaddings.calculateTopPadding()))
            }

            JobListItem(
                job = item,
                navigate = navigateToJobDetails
            )

            if (index == jobs.size - 1) {
                Spacer(modifier = Modifier.height(height = innerPaddings.calculateBottomPadding()))
            }
        }
    }
}
