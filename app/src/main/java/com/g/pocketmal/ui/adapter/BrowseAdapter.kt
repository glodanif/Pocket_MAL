package com.g.pocketmal.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.g.pocketmal.R
import com.g.pocketmal.loadUrl
import com.g.pocketmal.ui.viewmodel.BrowseItemViewModel
import java.util.*

class BrowseAdapter(context: Context) : SkeletonAdapter<BrowseItemViewModel>(context) {

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val holder: ViewHolder
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_filter_item, null)
            holder = ViewHolder(convertView)
            convertView!!.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val topItem = getItem(position)

        holder.title.text = topItem.title
        holder.synopsis.text = topItem.synopsis
        holder.type.text = topItem.mediaType
        holder.startDate.text = topItem.startDate

        holder.poster.loadUrl(topItem.poster)

        return convertView
    }

    fun addList(data: List<BrowseItemViewModel>?) {
        val list = data ?: ArrayList()
        getData().addAll(list)
    }

    class ViewHolder(view: View) {

        internal val poster: ImageView = view.findViewById(R.id.iv_poster)
        internal val title: TextView = view.findViewById(R.id.tv_title)
        internal val synopsis: TextView = view.findViewById(R.id.tv_synopsis)
        internal val type: TextView = view.findViewById(R.id.tv_type)
        internal val startDate: TextView = view.findViewById(R.id.tv_start_date)
    }
}
