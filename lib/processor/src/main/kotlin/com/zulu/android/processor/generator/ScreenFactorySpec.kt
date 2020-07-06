package com.zulu.android.processor.generator

import com.squareup.kotlinpoet.*
import com.zulu.android.processor.datamodel.ScreenData

class ScreenFactorySpec private constructor(
    private val data: ScreenData
) {
    private val className = "${data.className}Factory"

    fun build(): FileSpec {
        return FileSpec.builder(data.packageName, className)
            .addType(generateClassSpec())
            .build()
    }

    private fun generateClassSpec(): TypeSpec {
        return TypeSpec.classBuilder(className)
            .addSuperinterface(ClassName(FRAMEWORK_PACKAGE_NAME, "ScreenFactory"))
            .addProperty(generatePropertySpec())
            .addFunction(generateFunSpec())
            .build()
    }

    private fun generatePropertySpec(): PropertySpec {
        val getterFunSpec = FunSpec.getterBuilder()
            .addStatement("return %S", data.screenName)
            .build()

        return PropertySpec.builder("name", STRING, KModifier.OVERRIDE)
            .getter(getterFunSpec)
            .build()
    }

    private fun generateFunSpec(): FunSpec {
        return FunSpec.builder("create")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("context", ClassName(FRAMEWORK_PACKAGE_NAME, "ScreenContext"))
            .returns(ClassName(FRAMEWORK_PACKAGE_NAME, "Screen"))
            .addStatement("return %T(context)", ClassName(data.packageName, data.className))
            .build()
    }

    companion object {
        private const val FRAMEWORK_PACKAGE_NAME = "com.zulu.android"

        fun builder(data: ScreenData): ScreenFactorySpec {
            return ScreenFactorySpec(data)
        }
    }
}