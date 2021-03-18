package com.example.icemusic.data

data class FindingPageData(var adDataList:MutableList<AdvertData>?,
                           var typeEntryCellDataList:MutableList<TypeEntryCellData>?,
                           var recommendSongCellList:MutableList<RecommendSongCellData>?,
                           var personalSongCellList:MutableList<PersonalSongCellData>?)
