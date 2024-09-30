package com.ud.aplication.Logic

import android.annotation.SuppressLint
import androidx.compose.material3.rememberTimePickerState

class Operaciones {
    @SuppressLint("DefaultLocale")
    open fun formatTime(time: Int):String{
        val minutos = time / 60
        val segundos = time % 60
        return String.format("%02d:%02d", minutos, segundos)
    }
}