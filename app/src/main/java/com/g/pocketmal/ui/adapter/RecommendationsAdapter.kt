package com.g.pocketmal.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.g.pocketmal.R
import com.g.pocketmal.loadUrl
import com.g.pocketmal.ui.recommendations.RecommendedTitleViewEntity

class RecommendationsAdapter : RecyclerView.Adapter<RecommendationsAdapter.RecommendationViewHolder>() {

    var recommendations = listOf<RecommendedTitleViewEntity>()
    var listener: ((Int) -> Unit)? = null

    override fun getItemCount(): Int {
        return recommendations.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_recommended_title, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {

        val recommendedTitle = recommendations[position]

        holder.title.text = recommendedTitle.title
        holder.numRecommendations.text = recommendedTitle.recommendationsCount
        holder.details.text = recommendedTitle.details

        holder.poster.loadUrl(recommendedTitle.poster)

        holder.itemView.setOnClickListener { listener?.invoke(recommendedTitle.id) }
    }

    class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val title: TextView = itemView.findViewById(R.id.tv_title)
        internal val poster: ImageView = itemView.findViewById(R.id.iv_poster)
        internal val details: TextView = itemView.findViewById(R.id.tv_details)
        internal val numRecommendations: TextView = itemView.findViewById(R.id.tv_num_recommendations)
    }
}