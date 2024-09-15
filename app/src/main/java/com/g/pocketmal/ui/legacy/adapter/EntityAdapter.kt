package com.g.pocketmal.ui.legacy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.g.pocketmal.R
import com.g.pocketmal.domain.TitleType
import com.g.pocketmal.loadUrl
import com.g.pocketmal.ui.legacy.SkeletonActivity
import com.g.pocketmal.ui.legacy.adapter.filters.EntityFilter
import com.g.pocketmal.ui.legacy.popup.IncrementPopup
import com.g.pocketmal.ui.legacy.viewentity.RecordListViewModel
import com.g.pocketmal.util.Action
import com.g.pocketmal.util.EpisodeType
import java.util.*

//FIXME replace with RecyclerView
class EntityAdapter(private val context: SkeletonActivity, titles: List<com.g.pocketmal.ui.legacy.viewentity.RecordListViewModel>? = ArrayList()) : BaseAdapter() {

    var originalTitles: List<com.g.pocketmal.ui.legacy.viewentity.RecordListViewModel>
    var currentTitles: List<com.g.pocketmal.ui.legacy.viewentity.RecordListViewModel>

    val filter: EntityFilter

    private val inflater: LayoutInflater

    var incrementListener: ((Int, Int, TitleType, EpisodeType, Action) -> Unit)? = null

    private var actionEnabled = true
    private var isForeignList = false
    var simpleView = false
    var withTags = false

    init {

        val finalTitles = titles ?: ArrayList()

        this.originalTitles = finalTitles
        this.currentTitles = ArrayList(finalTitles)
        this.filter = EntityFilter(this)
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getItem(position: Int): Any {
        return currentTitles[position]
    }

    override fun getItemId(position: Int): Long {
        return currentTitles[position].seriesId.toLong()
    }

    override fun getCount(): Int {
        return currentTitles.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val holder: ViewHolder
        if (convertView == null) {
            convertView = inflater.inflate(if (simpleView) R.layout.item_simple_entity else R.layout.item_feed_item, null)
            holder = com.g.pocketmal.ui.legacy.adapter.EntityAdapter.ViewHolder(convertView)
            convertView!!.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val title = currentTitles[position]
        bindBasicLayout(holder, title)
        if (!simpleView && holder.poster != null) {
            holder.poster.loadUrl(title.seriesPosterUrl)
        }

        return convertView
    }

    private fun bindBasicLayout(holder: ViewHolder, title: RecordListViewModel) {

        holder.title.text = title.seriesTitle
        holder.status.text = title.seriesStatus
        holder.score.text = title.myScoreLabel
        holder.type.text = title.seriesMediaType

        holder.episodes.text = title.episodesLabel
        holder.episodesLabel.text = title.episodesName

        if (title.isSubEpisodesAvailable) {
            holder.subEpisodes.text = title.subEpisodesLabel
            holder.subEpisodesLabel.text = title.subEpisodesName
            holder.subEpisodesHolder.visibility = View.VISIBLE
        } else {
            holder.subEpisodesHolder.visibility = View.GONE
        }

        val tags = title.myTags
        if (tags != null && (withTags || isForeignList)) {
            holder.tagsHolder.visibility = View.VISIBLE
            holder.tags.text = tags
        } else {
            holder.tagsHolder.visibility = View.GONE
        }

        holder.reLabel.text = title.reLabel
        holder.reLabel.visibility = if (title.isRe) View.VISIBLE else View.GONE

        if (!isForeignList && actionEnabled) {

            val listener = View.OnClickListener { view ->

                val episodeType = if (view.id == R.id.ll_sub_episodes_holder)
                    title.subEpisodesType else title.episodesType
                val popup = IncrementPopup(context, episodeType)
                popup.incrementButtonClickListener = { type ->
                    when (type) {
                        EpisodeType.EPISODE -> incrementListener
                                ?.invoke(title.seriesId, title.myEpisodes + 1, TitleType.ANIME, EpisodeType.EPISODE, Action.ACTION_EPISODES)
                        EpisodeType.CHAPTER -> incrementListener
                                ?.invoke(title.seriesId, title.myEpisodes + 1, TitleType.MANGA, EpisodeType.CHAPTER, Action.ACTION_CHAPTERS)
                        EpisodeType.VOLUME -> incrementListener
                                ?.invoke(title.seriesId, title.mySubEpisodes + 1, TitleType.MANGA, EpisodeType.VOLUME, Action.ACTION_VOLUMES)
                    }
                }
                popup.show(view)
            }

            holder.episodesHolder.setOnClickListener(listener)
            if (title.isSubEpisodesAvailable) {
                holder.subEpisodesHolder.setOnClickListener(listener)
            }
        } else {
            holder.episodesHolder.setOnClickListener(null)
            if (title.isSubEpisodesAvailable) {
                holder.subEpisodesHolder.setOnClickListener(null)
            }
        }
    }

    fun setCurrentList(data: List<com.g.pocketmal.ui.legacy.viewentity.RecordListViewModel>) {
        this.currentTitles = data
    }

    fun setOriginalList(data: List<com.g.pocketmal.ui.legacy.viewentity.RecordListViewModel>) {
        setCurrentList(data)
        this.originalTitles = ArrayList<com.g.pocketmal.ui.legacy.viewentity.RecordListViewModel>(data)
    }

    fun setActionsEnabled(enabled: Boolean) {
        this.actionEnabled = enabled
    }

    fun setAsForeignList() {
        this.isForeignList = true
        setActionsEnabled(false)
    }

    internal class ViewHolder(view: View) {

        val poster: ImageView? = view.findViewById(R.id.iv_poster)
        val title: TextView = view.findViewById(R.id.tv_title)
        val status: TextView = view.findViewById(R.id.tv_status)
        val type: TextView = view.findViewById(R.id.tv_type)
        val score: TextView = view.findViewById(R.id.tv_score)
        val reLabel: TextView = view.findViewById(R.id.tv_rewatching_label)

        val episodes: TextView = view.findViewById(R.id.tv_episodes)
        val subEpisodes: TextView = view.findViewById(R.id.tv_sub_episodes)
        val episodesLabel: TextView = view.findViewById(R.id.tv_episodes_label)
        val subEpisodesLabel: TextView = view.findViewById(R.id.tv_sub_episodes_label)

        val episodesHolder: View = view.findViewById(R.id.ll_episodes_holder)
        val subEpisodesHolder: View = view.findViewById(R.id.ll_sub_episodes_holder)

        val tagsHolder: View = view.findViewById(R.id.cv_tags_holder)
        val tags: TextView = view.findViewById(R.id.tv_tags)
    }
}
