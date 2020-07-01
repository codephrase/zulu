package com.zulu.android

import android.os.Bundle

interface ObjectConverter {
    fun toBundle(obj: Any?): Bundle?
    fun fromBundle(bundle: Bundle?): Any?
}