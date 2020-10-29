package com.lenakurasheva.notes.common

import android.content.Context

// преобразует dp в px:
fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
fun Context.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()