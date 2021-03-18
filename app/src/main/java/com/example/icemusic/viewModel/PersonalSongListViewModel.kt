package com.example.icemusic.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.icemusic.R
import com.example.icemusic.databinding.PersonalSongListBinding

class PersonalSongListViewModel: BaseViewModel() {
    val TAG = "PersonalSongListVM"

    val personalSongTripleList : MutableLiveData<MutableList<PersonalSongCellTripleViewModel>> by lazy {
        MutableLiveData<MutableList<PersonalSongCellTripleViewModel>>().also {
            it.value = mutableListOf()
        }
    }

    init {
        layoutId = R.layout.personal_song_list
    }

    override fun bindData(viewDataBinding: ViewDataBinding, lifecycleOwner: LifecycleOwner) {
        viewDataBinding.takeIf { it is PersonalSongListBinding }.also {
            var personalSongListBinding = it as PersonalSongListBinding
            if(personalSongListBinding.personalSongRecyclerView.adapter==null){
                Log.i(TAG,"triple size: "+personalSongTripleList.value?.size)
                baseBindRecyclerView(personalSongListBinding.personalSongRecyclerView,lifecycleOwner,personalSongTripleList)
            }
        }
    }
}