package com.lizhaotailang.packman.common.ui.debug

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lizhaotailang.packman.common.database.PackmanDatabase
import com.lizhaotailang.packman.common.database.realmFilePath

class DebugViewModel(app: Application) : AndroidViewModel(app), DebugViewModelFeatures {

    val realmDatabase = PackmanDatabase(
        scope = viewModelScope,
        realmFilePath = realmFilePath(app = app)
    )

    private val delegate = DebugViewModelDelegate(
        scope = viewModelScope,
        database = realmDatabase
    )

    override fun insertHistoryIntoDatabase() {
        delegate.insertHistoryIntoDatabase()
    }

    companion object {

        const val KEY_DEBUG = "key_debug"

    }

}
