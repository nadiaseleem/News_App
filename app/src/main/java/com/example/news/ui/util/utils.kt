package com.example.news.ui.util

import android.content.Context

fun getCurrentLanguage(context: Context): String {
    val configuration = context.resources.configuration
    return configuration.locales[0].language
}