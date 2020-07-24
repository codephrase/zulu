package com.zulu.android.sample.screen.home

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Item(
    val type: ItemType,
    val title: String,
    val tag: String? = null
)