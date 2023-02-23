package com.lizhaotailang.packman.common.ui.debug

import com.lizhaotailang.packman.common.IOScheduler
import com.lizhaotailang.packman.common.data.History
import com.lizhaotailang.packman.common.data.toRealmInstant
import com.lizhaotailang.packman.common.database.PackmanDatabase
import com.lizhaotailang.packman.common.ui.new.Variant
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class DebugViewModelDelegate(
    private val scope: CoroutineScope,
    private val database: PackmanDatabase
) : DebugViewModelFeatures {

    override fun insertHistoryIntoDatabase() {
        scope.launch(IOScheduler) {
            try {
                database.realm.write {
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
                e.printStackTrace()
            }
        }
    }

}
