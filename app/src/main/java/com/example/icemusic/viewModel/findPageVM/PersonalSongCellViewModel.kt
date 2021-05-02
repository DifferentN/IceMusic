package com.example.icemusic.viewModel.findPageVM

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.example.icemusic.BR
import com.example.icemusic.R
import com.example.icemusic.data.PersonalSongCellData
import com.example.icemusic.viewModel.BaseViewModel

class PersonalSongCellViewModel : BaseViewModel() {

    var personalSongCellData:PersonalSongCellData? = null

    init {
        layoutId = R.layout.personal_song_cell
    }
    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner,viewModelStoreOwner: ViewModelStoreOwner) {
        viewDataBinding.setVariable(BR.obj,personalSongCellData)
    }

    override fun <Any> initialData(data: Any) {
        data?.let {
            var clazz = it::class.java
            if(PersonalSongCellData::class.java.isAssignableFrom(clazz)){
                personalSongCellData = data as PersonalSongCellData
            }
        }
    }
}