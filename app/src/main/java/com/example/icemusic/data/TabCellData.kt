package com.example.icemusic.data

import androidx.compose.ui.graphics.Color
import androidx.databinding.ObservableBoolean

data class TabCellData(var name:String,
                       var state:ObservableBoolean = ObservableBoolean(false),
                       var selectColor:Int,
                       var unSelectColor:Int= 0xFFFF0000.toInt())
