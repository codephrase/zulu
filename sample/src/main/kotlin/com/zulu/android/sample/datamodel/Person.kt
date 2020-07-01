package com.zulu.android.sample.datamodel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Person {
    var firstName: String? = null
    var lastName: String? = null
    var age: Int = 0
}