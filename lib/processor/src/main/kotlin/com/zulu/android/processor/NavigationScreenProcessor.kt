package com.zulu.android.processor

import com.google.auto.service.AutoService
import com.zulu.android.annotation.NavigationScreen
import com.zulu.android.processor.base.BaseProcessor
import com.zulu.android.processor.base.KAPT_KOTLIN_GENERATED_OPTION_NAME
import com.zulu.android.processor.datamodel.ScreenData
import com.zulu.android.processor.datamodel.ScreenInitializerData
import com.zulu.android.processor.extension.getAndroidManifest
import com.zulu.android.processor.generator.ScreenFactorySpec
import com.zulu.android.processor.generator.ScreenInitializerSpec
import java.io.File
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(KAPT_KOTLIN_GENERATED_OPTION_NAME)
class NavigationScreenProcessor : BaseProcessor() {
    private val screens = mutableListOf<ScreenData>()

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(NavigationScreen::class.java.name)
    }

    override fun process(
        set: MutableSet<out TypeElement>,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        val kotlinGeneratedDir = getKotlinGeneratedDir() ?: return false
        val kotlinGeneratedFile = File(kotlinGeneratedDir)

        if (!roundEnvironment.processingOver()) {
            roundEnvironment.getElementsAnnotatedWith(NavigationScreen::class.java)
                .forEach {
                    val annotation = it.getAnnotation(NavigationScreen::class.java)

                    val packageName = processingEnv.elementUtils.getPackageOf(it).toString()
                    val className = it.simpleName.toString()
                    val screenName = annotation.name

                    val data = ScreenData(
                        packageName,
                        className,
                        screenName
                    )

                    val fileSpec = ScreenFactorySpec.builder(data).build()
                    fileSpec.writeTo(kotlinGeneratedFile)

                    screens.add(data)
                }
        } else {
            val androidManifest = getAndroidManifest()
            androidManifest?.let {
                val data =
                    ScreenInitializerData(
                        it.packageName,
                        screens
                    )

                val fileSpec = ScreenInitializerSpec.builder(data).build()
                fileSpec.writeTo(kotlinGeneratedFile)
            }
        }

        return true
    }
}