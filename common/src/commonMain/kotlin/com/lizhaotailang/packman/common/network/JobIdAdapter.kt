package com.lizhaotailang.packman.common.network

import com.apollographql.apollo3.api.Adapter
import com.apollographql.apollo3.api.CustomScalarAdapters
import com.apollographql.apollo3.api.json.JsonReader
import com.apollographql.apollo3.api.json.JsonWriter

object JobIdAdapter : Adapter<String> {

    override fun fromJson(reader: JsonReader, customScalarAdapters: CustomScalarAdapters): String {
        return "#${reader.nextString()!!.replace("gid://gitlab/Ci::Build/", "")}"
    }

    override fun toJson(
        writer: JsonWriter,
        customScalarAdapters: CustomScalarAdapters,
        value: String
    ) {
        writer.value(value)
    }

}
