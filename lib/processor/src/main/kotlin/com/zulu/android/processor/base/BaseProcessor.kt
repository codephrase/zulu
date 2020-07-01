package com.zulu.android.processor.base

import javax.annotation.processing.AbstractProcessor

abstract class BaseProcessor : AbstractProcessor() {
    fun getKotlinGeneratedDir(): String? {
        return processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
    }
}