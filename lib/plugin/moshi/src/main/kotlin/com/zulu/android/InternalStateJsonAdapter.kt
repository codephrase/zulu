package com.zulu.android

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import java.lang.reflect.Type

class InternalStateJsonAdapter(
    moshi: Moshi
) : JsonAdapter<InternalState>() {
    private val options: JsonReader.Options =
        JsonReader.Options.of("upButtonEnabled", "title", "subtitle")

    private val nullableBooleanAdapter: JsonAdapter<Boolean?> =
        moshi.adapter(Boolean::class.javaObjectType, emptySet(), "upButtonEnabled")

    private val nullableStringAdapter: JsonAdapter<String?> =
        moshi.adapter(String::class.java, emptySet(), "title")

    override fun fromJson(reader: JsonReader): InternalState? {
        var upButtonEnabled: Boolean? = null
        var upButtonEnabledSet: Boolean = false
        var title: String? = null
        var titleSet: Boolean = false
        var subtitle: String? = null
        var subtitleSet: Boolean = false

        reader.beginObject()

        while (reader.hasNext()) {
            when (reader.selectName(options)) {
                0 -> {
                    upButtonEnabled = nullableBooleanAdapter.fromJson(reader)
                    upButtonEnabledSet = true
                }
                1 -> {
                    title = nullableStringAdapter.fromJson(reader)
                    titleSet = true
                }
                2 -> {
                    subtitle = nullableStringAdapter.fromJson(reader)
                    subtitleSet = true
                }
                -1 -> {
                    reader.skipName()
                    reader.skipValue()
                }
            }
        }

        reader.endObject()

        val result = InternalState()
        result.upButtonEnabled = if (upButtonEnabledSet) upButtonEnabled else result.upButtonEnabled
        result.title = if (titleSet) title else result.title
        result.subtitle = if (subtitleSet) subtitle else result.subtitle
        return result
    }

    override fun toJson(writer: JsonWriter, value: InternalState?) {
        if (value == null) {
            throw NullPointerException("value was null! Wrap in .nullSafe() to write nullable values.")
        }

        writer.beginObject()

        value.upButtonEnabled?.let {
            writer.name("upButtonEnabled")
            nullableBooleanAdapter.toJson(writer, it)
        }

        value.title?.let {
            writer.name("title")
            nullableStringAdapter.toJson(writer, it)
        }

        value.subtitle?.let {
            writer.name("subtitle")
            nullableStringAdapter.toJson(writer, it)
        }

        writer.endObject()
    }

    override fun toString(): String {
        return "JsonAdapter(InternalState)"
    }

    class Factory : JsonAdapter.Factory {
        override fun create(
            type: Type,
            annotations: MutableSet<out Annotation>,
            moshi: Moshi
        ): JsonAdapter<*>? {
            if (type == InternalState::class.java)
                return InternalStateJsonAdapter(moshi)

            return null
        }
    }
}