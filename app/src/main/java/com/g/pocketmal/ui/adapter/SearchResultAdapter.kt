package com.g.pocketmal.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.g.pocketmal.R
import com.g.pocketmal.loadUrl
import com.g.pocketmal.ui.viewmodel.SearchResultViewModel
import java.util.*

class SearchResultAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val typeItem = 0
    private val typeFooter = 1

    private val searchResult = ArrayList<SearchResultViewModel>()

    private var showFooter: Boolean = false

    var listener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == typeFooter) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.footer_lazy_load, parent, false)
            return FooterViewHolder(view)
        }

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_result, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (searchResult.size == 0) {
            return
        }

        if (holder is FooterViewHolder) {
            holder.itemView.visibility = if (showFooter) View.VISIBLE else View.GONE
            holder.progress.visibility = if (showFooter) View.VISIBLE else View.GONE
            return
        }

        val itemHolder = holder as ItemViewHolder
        val searchResultItem = searchResult[position]

        itemHolder.title.text = searchResultItem.title
        itemHolder.episodes.text = searchResultItem.episodes
        itemHolder.score.text = searchResultItem.score
        itemHolder.type.text = searchResultItem.type
        itemHolder.synopsis.text = searchResultItem.synopsis

        itemHolder.poster.loadUrl(searchResultItem.poster)

        holder.itemView.setOnClickListener { listener?.invoke(searchResultItem.id) }
    }

    override fun getItemCount(): Int {
        return searchResult.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionFooter(position)) {
            typeFooter
        } else typeItem
    }

    private fun isPositionFooter(position: Int): Boolean {
        return position == searchResult.size
    }

    fun setFooterVisible(showFooter: Boolean) {
        this.showFooter = showFooter
    }

    fun addItems(searchResult: List<SearchResultViewModel>) {
        this.searchResult.addAll(searchResult)
    }

    fun setItems(searchResult: List<SearchResultViewModel>) {
        this.searchResult.clear()
        addItems(searchResult)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val poster: ImageView = itemView.findViewById(R.id.iv_poster)
        internal val title: TextView = itemView.findViewById(R.id.tv_title)
        internal val synopsis: TextView = itemView.findViewById(R.id.tv_synopsis)
        internal val episodes: TextView = itemView.findViewById(R.id.tv_episodes)
        internal val type: TextView = itemView.findViewById(R.id.tv_type)
        internal val score: TextView = itemView.findViewById(R.id.tv_score)
    }

    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val progress: ProgressBar = itemView.findViewById(R.id.pb_lazy_load_progress)
    }
}
