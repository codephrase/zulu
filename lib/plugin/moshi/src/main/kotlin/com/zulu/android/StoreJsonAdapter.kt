package com.zulu.android

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import java.lang.reflect.Type

class StoreJsonAdapter(
    private val moshi: Moshi
) : JsonAdapter<Store>() {

    private val adapterCache: HashMap<Class<*>, JsonAdapter<*>> = hashMapOf()

    override fun fromJson(reader: JsonReader): Store? {
        if (reader.peek() == JsonReader.Token.BEGIN_OBJECT) {
            val map = hashMapOf<String, Any>()

            reader.beginObject()

            while (reader.hasNext()) {
                val key = reader.nextName()

                reader.beginArray()

                val type = Class.forName(reader.nextString())
                val value = resolveAdapter(type).fromJson(reader)

                reader.endArray()

                value?.let {
                    map[key] = it
                }
            }

            reader.endObject()

            return Store(map)
        } else {
            throw IllegalStateException(
                "Expected an object but was " + reader.peek() + " at path " + reader.path
            )
        }
    }

    override fun toJson(writer: JsonWriter, store: Store?) {
        store?.let {
            writer.beginObject()

            it.toMap().forEach { (key, value) ->
                writer.name(key)

                writer.beginArray()

                resolveAdapter(String::class.java).toJson(writer, value.javaClass.name)
                resolveAdapter(value.javaClass).toJson(writer, value)

                writer.endArray()
            }

            writer.endObject()
        } ?: run {
            writer.nullValue()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> resolveAdapter(type: Class<T>): JsonAdapter<T> {
        if (adapterCache.containsKey(type))
            return adapterCache[type] as JsonAdapter<T>

        val adapter = moshi.adapter(type)
        adapterCache[type] = adapter
        return adapter
    }

    class Factory : JsonAdapter.Factory {
        override fun create(
            type: Type,
            annotations: MutableSet<out Annotation>,
            moshi: Moshi
        ): JsonAdapter<*>? {
            if (type == Store::class.java)
                return StoreJsonAdapter(moshi)

            return null
        }
    }
}