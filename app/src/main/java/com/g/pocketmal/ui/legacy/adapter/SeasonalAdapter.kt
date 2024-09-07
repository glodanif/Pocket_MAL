package com.g.pocketmal.ui.legacy.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.g.pocketmal.R
import com.g.pocketmal.loadUrl
import java.util.*

class SeasonalAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var seasonalList: List<com.g.pocketmal.ui.legacy.viewentity.SeasonalSectionViewModel> = ArrayList()

    var listener: ((com.g.pocketmal.ui.legacy.viewentity.SeasonalAnimeViewModel) -> Unit)? = null

    private var size: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_section_header, parent, false)
            return SectionViewHolder(view)
        }

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_seasonal, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (seasonalList.isEmpty()) {
            return
        }

        if (holder is SectionViewHolder) {

            var generalVolume = 0

            for (i in seasonalList.indices) {

                val section = seasonalList[i]

                val sectionVolume = section.items.size + 1
                if (position == generalVolume) {
                    holder.header.text = section.title
                    return
                }
                generalVolume += sectionVolume
            }
            return
        }

        var generalVolume = 0

        for (i in seasonalList.indices) {

            val section = seasonalList[i]

            val sectionVolume = section.items.size + 1
            if (position > generalVolume && position < generalVolume + sectionVolume) {

                val innerPosition = position - generalVolume - 1
                val itemHolder = holder as ItemViewHolder

                val item = section.items[innerPosition]
                bindTitle(itemHolder, item)
            }
            generalVolume += sectionVolume
        }
    }

    override fun getItemCount(): Int {
        return size
    }

    override fun getItemViewType(position: Int): Int {

        var generalVolume = 0

        for (i in seasonalList.indices) {

            val section = seasonalList[i]

            val sectionVolume = section.items.size + 1
            if (position == generalVolume) {
                return TYPE_HEADER
            }
            generalVolume += sectionVolume
        }

        return TYPE_ITEM
    }

    fun setItems(seasonalList: List<com.g.pocketmal.ui.legacy.viewentity.SeasonalSectionViewModel>?) {

        this.seasonalList = seasonalList ?: ArrayList()

        this.size = 0
        for (section in this.seasonalList) {
            this.size += section.items.size + 1
        }
    }

    private fun bindTitle(holder: ItemViewHolder, title: com.g.pocketmal.ui.legacy.viewentity.SeasonalAnimeViewModel) {

        holder.title.text = Html.fromHtml(title.title)
        holder.info.text = title.info
        holder.genres.text = title.genres
        holder.synopsis.text = title.synopsis
        holder.airing.text = title.airing
        holder.members.text = title.members
        holder.score.text = title.score

        holder.poster.loadUrl(title.poster)

        holder.itemView.setOnClickListener {
            listener?.invoke(title)
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val poster: ImageView = itemView.findViewById(R.id.iv_poster)
        internal val title: TextView = itemView.findViewById(R.id.tv_title)
        internal val info: TextView = itemView.findViewById(R.id.tv_info)
        internal val score: TextView = itemView.findViewById(R.id.tv_score)
        internal val genres: TextView = itemView.findViewById(R.id.tv_genres)
        internal val synopsis: TextView = itemView.findViewById(R.id.tv_synopsis)
        internal val airing: TextView = itemView.findViewById(R.id.tv_airing)
        internal val members: TextView = itemView.findViewById(R.id.tv_members)
    }

    class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val header: TextView = itemView.findViewById(R.id.tv_header)
    }

    companion object {
        const val TYPE_ITEM = 0
        const val TYPE_HEADER = 1
    }
}
