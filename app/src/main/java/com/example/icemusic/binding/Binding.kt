package com.example.icemusic.binding

import android.annotation.TargetApi
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.adapters.ListenerUtil
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.icemusic.R
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
val TAG = "Binding"
@BindingConversion
fun convertColorValueToDrawable(color: Int) = ColorDrawable(color)

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
