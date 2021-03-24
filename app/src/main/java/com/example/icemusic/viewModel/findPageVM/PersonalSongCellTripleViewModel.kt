package com.example.icemusic.viewModel.findPageVM

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.icemusic.BR
import com.example.icemusic.R
import com.example.icemusic.data.PersonalSongCellData
import com.example.icemusic.viewModel.BaseViewModel

class PersonalSongCellTripleViewModel : BaseViewModel() {

    val personalSongCellDataList : MutableLiveData<MutableList<PersonalSongCellData>> by lazy {
        MutableLiveData<MutableList<PersonalSongCellData>>().also {
            it.value = mutableListOf()
        }
    }

    init {
        layoutId = R.layout.personal_song_cell_triple
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner) {
        personalSongCellDataList.observe(lifecycleOwner, Observer {
            viewDataBinding.setVariable(BR.obj,it)
            viewDataBinding.executePendingBindings()
        })
    }
}