package com.zulu.android.processor.extension

import com.zulu.android.processor.base.AndroidManifest
import com.zulu.android.processor.base.BaseProcessor
import java.io.File
import java.util.regex.Pattern
import javax.xml.parsers.DocumentBuilderFactory

fun BaseProcessor.getAndroidManifest(): AndroidManifest? {
    val rootDir = getRootDir() ?: return null

    val file = File(rootDir, "/src/main/AndroidManifest.xml")
    if (file.exists()) {
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()

        try {
            val documentBuilder = documentBuilderFactory.newDocumentBuilder()
            val document = documentBuilder.parse(file)

            val documentElement = document.documentElement
            documentElement.normalize()

            val packageName = documentElement.getAttribute("package")

            return AndroidManifest(
                packageName
            )
        } catch (exception: Exception) {

        }
    }

    return null
}

fun BaseProcessor.getRootDir(): String? {
    val kotlinGeneratedDir = getKotlinGeneratedDir() ?: return null

    val pattern = Pattern.compile("""^(.*)[\\/]build[\\/]generated[\\/]source[\\/](.*)$""")
    val matcher = pattern.matcher(kotlinGeneratedDir)

    if (matcher.matches())
        return matcher.group(1)

    return null
}