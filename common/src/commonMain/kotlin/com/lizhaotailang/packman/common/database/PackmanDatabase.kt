package com.lizhaotailang.packman.common.database

import com.lizhaotailang.packman.common.data.History
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PackmanDatabase(
    scope: CoroutineScope,
    realmFilePath: String
) {

    private val _historiesFlow: MutableStateFlow<List<History>> =
        MutableStateFlow(value = emptyList())
    val historiesFlow: StateFlow<List<History>> = _historiesFlow

    val realm: Realm

    init {
        val cfg = RealmConfiguration.Builder(schema = setOf(History::class))
            .migration(migration = PackmanRealmMigration())
            .directory(directoryPath = realmFilePath)
            .schemaVersion(schemaVersion = PACKMAN_REALM_SCHEMA_VERSION)
            .build()

        realm = Realm.open(configuration = cfg)

        @Suppress("DeferredResultUnused")
        scope.async {
            realm.query<History>()
                .sort(
                    property = "startedAt",
                    sortOrder = Sort.DESCENDING
                )
                .asFlow()
                .collect { result ->
                    _historiesFlow.emit(
                        when (result) {
                            is InitialResults -> {
                                result.list
                            }
                            is UpdatedResults -> {
                                result.list
                            }
                        }
                    )
                }
        }
    }

    companion object {

        private const val PACKMAN_REALM_SCHEMA_VERSION = 1L

    }

}
