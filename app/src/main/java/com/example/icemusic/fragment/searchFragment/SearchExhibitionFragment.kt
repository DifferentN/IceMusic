package com.example.icemusic.fragment.searchFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.icemusic.databinding.SearchExhibitionBinding

class SearchExhibitionFragment:Fragment() {

    lateinit var searchExhibitionBinding: SearchExhibitionBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var searchExhibitionBinding = SearchExhibitionBinding.inflate(inflater,container,false)
        return searchExhibitionBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}