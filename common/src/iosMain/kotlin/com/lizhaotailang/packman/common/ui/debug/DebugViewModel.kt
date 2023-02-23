package com.lizhaotailang.packman.common.ui.debug

import com.lizhaotailang.packman.common.database.PackmanDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import platform.Foundation.NSHomeDirectory

class DebugViewModel : DebugViewModelFeatures {

    private val viewModelScope = CoroutineScope(Dispatchers.Main) + Dispatchers.Main.immediate

    val database = PackmanDatabase(
        scope = viewModelScope,
        realmFilePath = "${NSHomeDirectory()}/Documents/packman.realm"
    )

    private val delegate = DebugViewModelDelegate(
        scope = viewModelScope,
        database = database
    )

    override fun insertHistoryIntoDatabase() {
        delegate.insertHistoryIntoDatabase()
    }

}
