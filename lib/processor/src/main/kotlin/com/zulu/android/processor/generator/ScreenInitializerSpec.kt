package com.zulu.android.processor.generator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.zulu.android.processor.datamodel.ScreenInitializerData

class ScreenInitializerSpec private constructor(
    private val data: ScreenInitializerData
) {
    private val className = "ScreenInitializer"

    fun build(): FileSpec {
        return FileSpec.builder(data.packageName, className)
            .addType(generateClassSpec())
            .build()
    }

    private fun generateClassSpec(): TypeSpec {
        return TypeSpec.objectBuilder(className)
            .addFunction(generateFunSpec())
            .build()
    }

    private fun generateFunSpec(): FunSpec {
        return FunSpec.builder("run")
            .apply {
                data.screens.forEach {
                    addStatement(
                        "%T.registerScreen<%T>()",
                        ClassName(FRAMEWORK_PACKAGE_NAME, "NavigationManager"),
                        ClassName(it.packageName, "${it.className}Factory")
                    )
                }
            }
            .build()
    }

    companion object {
        private const val FRAMEWORK_PACKAGE_NAME = "com.zulu.android"

        fun builder(data: ScreenInitializerData): ScreenInitializerSpec {
            return ScreenInitializerSpec(
                data
            )
        }
    }
}