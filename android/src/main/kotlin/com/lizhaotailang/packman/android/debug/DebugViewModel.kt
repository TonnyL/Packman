package com.lizhaotailang.packman.android.debug

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lizhaotailang.packman.common.data.History
import com.lizhaotailang.packman.common.data.toRealmInstant
import com.lizhaotailang.packman.common.database.PackmanDatabase
import com.lizhaotailang.packman.common.database.realmFilePath
import com.lizhaotailang.packman.common.ui.new.Variant
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class DebugViewModel(app: Application) : AndroidViewModel(app) {

    val realmDatabase = PackmanDatabase(
        scope = viewModelScope,
        realmFilePath = realmFilePath(app = app)
    )

    fun insertHistoryIntoDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                realmDatabase.realm.write {
                    this.copyToRealm(
                        History().apply {
                            branch = Clock.System.now().toString()
                            variants = realmListOf(
                                Variant.OfficialDebug.ordinal,
                                Variant.GooglePlayRelease.ordinal
                            )
                            startedAt = Clock.System.now().toRealmInstant()
                            pipelineId = 29028
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e("DebugViewModel", null, e)
            }
        }
    }

    companion object {

        const val KEY_DEBUG = "key_debug"

    }

}
