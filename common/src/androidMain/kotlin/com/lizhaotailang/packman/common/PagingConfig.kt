package com.lizhaotailang.packman.common

import androidx.paging.PagingConfig
import com.lizhaotailang.packman.common.ui.MAX_SIZE_OF_PAGED_LIST
import com.lizhaotailang.packman.common.ui.PER_PAGE

val defaultPagingConfig = PagingConfig(
    pageSize = PER_PAGE,
    maxSize = MAX_SIZE_OF_PAGED_LIST,
    enablePlaceholders = true
)
