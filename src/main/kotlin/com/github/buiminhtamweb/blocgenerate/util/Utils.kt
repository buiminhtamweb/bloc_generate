package com.github.buiminhtamweb.blocgenerate.util

import java.util.*

@OptIn(ExperimentalStdlibApi::class)
fun String.snakeToLowerCamelCase() =
    toCamelCase().replaceFirstChar { if (it.isLowerCase())   it.toString() else it.lowercase()}

@OptIn(ExperimentalStdlibApi::class)
fun String.toCamelCase() =
    split('_').joinToString("") { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }

fun String.toSnakeCase() = replace(humps, "_").toLowerCase()
private val humps = "(?<=.)(?=\\p{Upper})".toRegex()

