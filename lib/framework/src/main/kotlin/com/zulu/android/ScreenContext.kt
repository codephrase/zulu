package com.zulu.android

import android.content.Context
import androidx.lifecycle.LifecycleOwner

class ScreenContext(
    val context: Context,
    val lifecycleOwner: LifecycleOwner,
    val id: String,
    val name: String,
    val store: Store
)