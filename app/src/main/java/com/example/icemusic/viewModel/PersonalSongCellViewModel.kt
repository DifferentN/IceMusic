package com.example.icemusic.viewModel

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.example.icemusic.BR
import com.example.icemusic.R
import com.example.icemusic.data.PersonalSongCellData

class PersonalSongCellViewModel : BaseViewModel() {

    var personalSongCellData:PersonalSongCellData? = null

    init {
        layoutId = R.layout.personal_song_cell
    }
    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner) {
        viewDataBinding.setVariable(BR.obj,personalSongCellData)
    }
}