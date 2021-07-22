package com.github.buiminhtamweb.blocgenerate.util

fun String.toCamelCase() =
    split('_').joinToString("", transform = String::capitalize)

fun String.toSnakeCase() = replace(humps, "_").toLowerCase()
private val humps = "(?<=.)(?=\\p{Upper})".toRegex()

