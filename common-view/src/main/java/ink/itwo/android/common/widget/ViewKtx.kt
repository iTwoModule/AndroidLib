package ink.itwo.android.common.widget

import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.view.children
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.UpFetchModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
/** Created by wang on 2020/5/26. */
fun TabLayout.setupViewPageForEnlarge(viewPage: ViewPager2, titles: MutableList<String>, colorSelect: Int = 0xFF222222.toInt(), colorNormal: Int = 0xE6222222.toInt(), textSizeSelect: Float = 24F, textSizeNormal: Float = 14F, alpha: Float = 0.9F, itemWidth: Int? = null) {
    TabLayoutMediator(this, viewPage, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
        tab.customView = TextView(this.context).apply {
            text = titles[position]
            textSize = if (position == 0) textSizeSelect else textSizeNormal
            setTextColor(if (position == 0) colorSelect else colorNormal)
            gravity = Gravity.CENTER
            this.alpha = if (position == 0) 1F else alpha
//            setBackgroundColor(Color.BLACK)
        }
    }).attach()
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            var tv = tab?.customView as? TextView
            tv?.textSize = textSizeNormal
            tv?.alpha = alpha
            tv?.setTextColor(colorNormal)
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            var tv = tab?.customView as? TextView
            tv?.textSize = textSizeSelect
            tv?.alpha = 1F
            tv?.setTextColor(colorSelect)
        }
    })
    itemWidth?.let { w ->
        post {
            try {
                val viewGroup = getChildAt(0) as ViewGroup
                viewGroup?.children.forEach {
                    val tabView = it as TabLayout.TabView
                    val layoutParams = tabView.layoutParams as LinearLayout.LayoutParams
                    layoutParams?.width = w
                    layoutParams?.let { p -> tabView.layoutParams = p }
                    tabView.setPadding(0,0,0,0)

//                    val customView = tabView.tab?.customView as TextView
//                    val lp = customView.layoutParams as LinearLayout.LayoutParams
//                    lp.width = w
//                    customView.layoutParams=lp
//                    lp.marginEnd = 0
//                    lp.marginStart=0
//                    customView.setPadding(0,0,0,0)
                }
            } catch (e: Exception) {
            }
        }
    }
}


fun <T> ViewPager2.toBanner(@LayoutRes layoutResId: Int, data: MutableList<T>, delayMillis: Long = 3000, enableLoop:Boolean=true, _convert: (holder: BaseViewHolder, item: T) -> Unit) {
    var dataBanner = mutableListOf<T>()
    dataBanner.addAll(data)
    dataBanner.addAll(data)
    dataBanner.addAll(data)
    var adapterBanner = object : BaseQuickAdapter<T, BaseViewHolder>(layoutResId, dataBanner)/*, LoadMoreModule*/,
        UpFetchModule {
        override fun convert(holder: BaseViewHolder, item: T) {
            _convert(holder, item)
        }
    }

//    adapterBanner.loadMoreModule.setOnLoadMoreListener {
//        dataBanner.addAll(data)
//        post { adapterBanner.loadMoreModule.loadMoreComplete() }
//    }
    adapterBanner.upFetchModule.isUpFetchEnable = true
    adapterBanner.upFetchModule.setOnUpFetchListener {
        post { adapterBanner.addData(0, data) }
    }

    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (position >= dataBanner.size - 2) {
                post {
                    dataBanner.addAll(data)
                    adapterBanner.notifyDataSetChanged()
                }
            }
            /*else if (position <= 2) {
                post {
                    dataBanner.addAll(0, data)
                    adapterBanner.notifyDataSetChanged()
                }
            }*/
        }
    })
    adapter = adapterBanner
    setCurrentItem(data.size, false)
    if (delayMillis > 0) {
        var runnable: Runnable = object : Runnable {
            override fun run() {
                currentItem++
                postDelayed(this, delayMillis)
            }
        }
        postDelayed(runnable, delayMillis)
    }
}

