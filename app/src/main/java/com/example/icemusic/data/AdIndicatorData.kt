package com.example.icemusic.data

import android.graphics.Color
import androidx.databinding.ObservableBoolean

data class AdIndicatorData(var state:ObservableBoolean = ObservableBoolean(false),
                           var selectColor:Int = Color.GRAY.toInt(),
                           var unSelectColor:Int = Color.WHITE.toInt())
