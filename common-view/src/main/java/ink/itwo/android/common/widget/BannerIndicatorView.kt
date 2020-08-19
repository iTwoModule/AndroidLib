package ink.itwo.android.common.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ink.itwo.android.common.control.R


/** banner 轮播指示器
 *  Created by wang on 2020/5/8. */
open class BannerIndicatorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    protected  var paint: Paint = Paint().apply { isAntiAlias = true }
    protected  var colorSelect: Int = 0xFFFFFFFF.toInt()
    protected  var colorNormal: Int = 0x88FFFFFF.toInt()
    protected var position: Int = 0
    protected  var total: Int = 0
    protected var radius: Float = 6F
    protected  var space: Float = 6F

    var listener:((Int)->Unit)?=null

    init {
        val array = context?.obtainStyledAttributes(attrs, R.styleable.BannerIndicatorView)
        array?.let {
            colorSelect = it.getColor(R.styleable.BannerIndicatorView_color_select, colorSelect)
            colorNormal = it.getColor(R.styleable.BannerIndicatorView_color_normal, colorNormal)
            radius = it.getDimension(R.styleable.BannerIndicatorView_radius, 6F)
            space = it.getDimension(R.styleable.BannerIndicatorView_space, 6F)
            it.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (total != 0) {
            setMeasuredDimension(((radius * 2 + space) * total - space).toInt(), (radius * 2).toInt())
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (total <= 1 || position < 0) return
        for (i in 0 until total) {
            if (position == i) {
                paint.color = colorSelect
            } else {
                paint.color = colorNormal
            }
            canvas.drawCircle(radius + i * (radius * 2 + space), radius, radius, paint)
        }
    }
    fun setTotalCount(totalCount: Int) {
        total = totalCount
        requestLayout()
    }
    fun onPageSelected(position: Int) {
        this.position = position
        postInvalidate()
    }

}

