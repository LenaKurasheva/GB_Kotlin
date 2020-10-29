package com.lenakurasheva.notes.common

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import com.lenakurasheva.notes.data.entity.Color
import ru.geekbrains.gb_kotlin.R

fun getColorList(): List<Int>{
    return Color.values().map { it.getColorRes() }
}

fun Int.toColor() : Color = when (this) {
     R.color.color_white -> Color.WHITE
     R.color.color_yello -> Color.YELLOW
     R.color.color_green -> Color.GREEN
     R.color.color_blue -> Color.BLUE
     R.color.color_red -> Color.RED
     R.color.color_violet -> Color.VIOLET
    else -> Color.WHITE
}

fun Int.getColorFromRes(context : Context) = ResourcesCompat.getColor(context.resources, this, null)