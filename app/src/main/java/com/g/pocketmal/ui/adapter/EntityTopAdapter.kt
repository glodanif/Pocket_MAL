package com.g.pocketmal.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.g.pocketmal.R
import com.g.pocketmal.loadUrl
import com.g.pocketmal.ui.viewmodel.RankedItemViewModel
import java.util.*

class EntityTopAdapter(context: Context) : SkeletonAdapter<RankedItemViewModel>(context) {

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val holder: ViewHolder
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_entity_top, null)
            holder = ViewHolder(convertView)
            convertView!!.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val topItem = getItem(position)

        holder.name.text = topItem.title
        holder.number.text = topItem.position
        holder.details.text = topItem.details
        holder.members.text = topItem.members

        holder.poster.loadUrl(topItem.poster)

        return convertView
    }

    fun addList(data: List<RankedItemViewModel>?) {
        val list = data ?: ArrayList()
        getData().addAll(list)
    }

    class ViewHolder(view: View) {

        internal val poster: ImageView = view.findViewById(R.id.iv_poster)
        internal val name: TextView = view.findViewById(R.id.tv_name)
        internal val number: TextView = view.findViewById(R.id.tv_number)
        internal val details: TextView = view.findViewById(R.id.tv_details)
        internal val members: TextView = view.findViewById(R.id.tv_members)
    }
}
