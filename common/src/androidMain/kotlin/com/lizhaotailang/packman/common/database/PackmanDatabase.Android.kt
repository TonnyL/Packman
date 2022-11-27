package com.lizhaotailang.packman.common.database

import android.app.Application

fun realmFilePath(app: Application): String {
    return app.filesDir.resolve("packman.realm").absolutePath
}
