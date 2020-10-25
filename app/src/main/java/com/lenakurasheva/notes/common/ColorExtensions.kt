package com.lenakurasheva.notes.common

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import com.lenakurasheva.notes.data.entity.Color
import ru.geekbrains.gb_kotlin.R

fun Color.getColorRes() : Int = when (this) {
    Color.WHITE -> R.color.color_white
    Color.YELLOW -> R.color.color_yello
    Color.GREEN -> R.color.color_green
    Color.BLUE -> R.color.color_blue
    Color.RED -> R.color.color_red
    Color.VIOLET -> R.color.color_violet
}

fun Color.getColorInt(context : Context) = ResourcesCompat.getColor(context.resources, getColorRes(), null)


