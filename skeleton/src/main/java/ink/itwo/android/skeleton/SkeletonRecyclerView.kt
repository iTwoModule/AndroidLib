package ink.itwo.android.skeleton

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

/** Created by wang on 2020/9/25. */
class SkeletonRecyclerView : Skeleton<RecyclerView> {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterOrigin: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private val adapterSkeleton: RecyclerView.Adapter<RecyclerView.ViewHolder> by lazy {
        object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                var v = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
                return VH(v)
            }

            override fun getItemCount(): Int = this@SkeletonRecyclerView.itemCount

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            }
        }
    }
    private var layoutId by Delegates.notNull<Int>()

    var itemCount = 10

    class VH(v: View) : RecyclerView.ViewHolder(v)

    override fun bind(v: RecyclerView): SkeletonRecyclerView {
        recyclerView = v
        adapterOrigin = recyclerView.adapter ?: throw RuntimeException("recyclerView adapter is null")
        return this
    }


    override fun load(layoutId: Int): SkeletonRecyclerView {
        this.layoutId = layoutId
        return this
    }

    override fun show(): SkeletonRecyclerView {
        recyclerView.adapter = adapterSkeleton
        return this
    }

    override fun hide() {
        recyclerView.adapter = adapterOrigin
        adapterOrigin.notifyDataSetChanged()
    }
}