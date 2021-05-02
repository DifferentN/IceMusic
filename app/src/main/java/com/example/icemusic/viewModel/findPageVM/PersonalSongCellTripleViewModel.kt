package com.example.icemusic.viewModel.findPageVM

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import com.example.icemusic.BR
import com.example.icemusic.R
import com.example.icemusic.data.PersonalSongCellData
import com.example.icemusic.data.PersonalSongTripleCellData
import com.example.icemusic.viewModel.BaseViewModel

class PersonalSongCellTripleViewModel : BaseViewModel() {

    val personalSongCellTripleCellData : MutableLiveData<PersonalSongTripleCellData> by lazy {
        MutableLiveData<PersonalSongTripleCellData>()
    }

    init {
        layoutId = R.layout.personal_song_cell_triple
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner,viewModelStoreOwner: ViewModelStoreOwner) {
        personalSongCellTripleCellData.observe(lifecycleOwner, Observer {
            viewDataBinding.setVariable(BR.obj,it.tripleDatas)
            viewDataBinding.executePendingBindings()
        })
    }

    override fun <Any> initialData(data: Any) {
        data?.let {
            var clazz = it::class.java
            if(PersonalSongTripleCellData::class.java.isAssignableFrom(clazz)){
                personalSongCellTripleCellData.value = data as PersonalSongTripleCellData
            }
        }
    }
}