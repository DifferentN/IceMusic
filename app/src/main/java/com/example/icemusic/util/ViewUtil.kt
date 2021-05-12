package com.example.icemusic.util

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ViewUtil {

    companion object{

        /**
         * 获取ViewGroup中的RecyclerView
         * @param vg ViewGroup
         * @return RecyclerView?
         */
        fun searchRecyclerView(vg: ViewGroup): RecyclerView?{
            for(i in 0 until vg.childCount){
                var view = vg.getChildAt(i)
                if(view is RecyclerView){
                    return view
                }else if(view is ViewGroup){
                    var recyclerView = searchRecyclerView(view)
                    if(recyclerView!=null){
                        return recyclerView
                    }
                }
            }
            return null
        }

        fun dp2px(context: Context,dp:Float):Int{
            var density = context.resources.displayMetrics.density
            return (dp*density+05f).toInt()
        }
    }

}