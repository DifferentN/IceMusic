package com.example.icemusic.binding

import android.annotation.TargetApi
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.compose.ui.graphics.imageFromResource
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseMethod
import androidx.databinding.adapters.ListenerUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.Rotate
import com.example.icemusic.R
import com.example.icemusic.musicPlayManager.musicClient.model.SongMetaData
import com.example.icemusic.view.ProcessPlayImageView
import com.example.icemusic.view.RotateImageView
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
val TAG = "Binding"

@BindingConversion
fun convertColorValueToDrawable(color: Int) = ColorDrawable(color)

@BindingAdapter("android:drawableId")
fun loadImageByDrawableId(imageView:ImageView,drawableId:Int){
    var drawable = imageView.resources.getDrawable(drawableId,null)
    imageView.background = drawable
//    Picasso.get().load(drawableId).into(imageView)
}

@BindingAdapter("android:imageUrl")
fun loadImage(imageView: ImageView, imageUrl: String?) {
    imageUrl?.let {
        Picasso.get().load(it).fit().into(imageView)
//        Glide.with(imageView.context).load(imageUrl).into(imageView)
    }
}

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
interface OnTabSelected {
    fun onTabSelected(tab: TabLayout.Tab?)
}

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
interface OnTabUnselected {
    fun onTabUnselected(tab: TabLayout.Tab?)
}

@BindingAdapter("android:onTabSelected", "android:onTabUnselected", requireAll = false)
fun setTabSelectedListener(tabLayout: TabLayout, selected: OnTabSelected?, unselected: OnTabUnselected?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
        var newListener: TabLayout.OnTabSelectedListener?
        newListener = if (selected == null && unselected == null) {
            null
        } else {
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    selected?.onTabSelected(tab)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    unselected?.onTabUnselected(tab)
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            }
        }

        val oldListener: TabLayout.OnTabSelectedListener? = ListenerUtil.trackListener(tabLayout, newListener, R.id.onTabSelectedListener)
        if (oldListener != null) {
            tabLayout.removeOnTabSelectedListener(oldListener)
        }
        if (newListener != null) {
            tabLayout.addOnTabSelectedListener(newListener)
        }
    }
}

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
interface OnPageSelected {
    fun onPageSelected(position: Int)
}

@BindingAdapter("android:onPageChange")
fun setOnPageChangeListener(viewPager2: ViewPager2, pageSelected: OnPageSelected) {
    var newListener: ViewPager2.OnPageChangeCallback?
    newListener = if (pageSelected == null) {
        null
    } else {
        object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                pageSelected.onPageSelected(position)
            }
        }
    }
    var oldListener = ListenerUtil.trackListener(viewPager2, newListener, R.id.onPageChangeListener)
    if (oldListener != null) {
        viewPager2.unregisterOnPageChangeCallback(oldListener)
    }
    if (newListener != null) {
        viewPager2.registerOnPageChangeCallback(newListener)
    }
}

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
interface OnScrollStateChanged {
    fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int)
}

@BindingAdapter("android:onScrollStateChanged")
fun setOnScrollStateChangeListener(recyclerView: RecyclerView, stateChange: OnScrollStateChanged) {
    var newListener: RecyclerView.OnScrollListener?
    newListener = if (stateChange == null) {
        null
    } else {
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                stateChange.onScrollStateChanged(recyclerView,newState)
            }
        }
    }
    var oldListener = ListenerUtil.trackListener(recyclerView, newListener, R.id.onScrollStateChanged)
    if (oldListener != null) {
        recyclerView.removeOnScrollListener(oldListener)
    }
    if (newListener != null) {
        recyclerView.addOnScrollListener(newListener)
    }
}

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
interface OnEditTextFocusChange {
    fun onEditTextFocusChange(view: View?, hasFocus: Boolean)
}

@BindingAdapter(value = ["android:onFocusChange"],requireAll = false)
fun setEditTextFocusChangeListener(editText: EditText,change: OnEditTextFocusChange){
    var newListener: View.OnFocusChangeListener? = if(change==null){
        null
    }else{
        object :View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                change.onEditTextFocusChange(v,hasFocus)
            }
        }
    }
    if(newListener!=null){
        editText.onFocusChangeListener = newListener
    }
}

@BindingAdapter("app:songMetaData")
fun setSongMetaData(processPlayImageView:ProcessPlayImageView,songMetaData:SongMetaData?){
    songMetaData?.let {
        processPlayImageView.setSongMetaData(it)
    }
}