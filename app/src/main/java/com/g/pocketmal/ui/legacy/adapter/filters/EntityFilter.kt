package com.g.pocketmal.ui.legacy.adapter.filters

import android.widget.Filter

import java.util.ArrayList

class EntityFilter(private val adapter: com.g.pocketmal.ui.legacy.adapter.EntityAdapter) : Filter() {

    override fun performFiltering(constraint: CharSequence): FilterResults {

        val searchText = constraint.toString().toLowerCase()
        val filteredData = getFilteredList(searchText)

        val result = FilterResults()
        result.values = filteredData
        result.count = filteredData.size
        return result
    }

    override fun publishResults(constraint: CharSequence, results: FilterResults) {

        val filteredData = ArrayList<com.g.pocketmal.ui.legacy.viewentity.RecordListViewModel>()

        val result = results.values as List<*>
        for (viewModel in result) {
            if (viewModel is com.g.pocketmal.ui.legacy.viewentity.RecordListViewModel) {
                filteredData.add(viewModel)
            }
        }

        adapter.setCurrentList(filteredData)
        adapter.notifyDataSetChanged()
    }

    private fun getFilteredList(searchText: String): List<com.g.pocketmal.ui.legacy.viewentity.RecordListViewModel> {

        val filteredData = ArrayList<com.g.pocketmal.ui.legacy.viewentity.RecordListViewModel>()

        return if (searchText.isNotEmpty()) {
            val d2 = ArrayList(adapter.originalTitles)
            for (data in d2) {
                if (data.seriesTitle.toLowerCase().contains(searchText)) {
                    filteredData.add(data)
                }
            }
            filteredData
        } else {
            ArrayList(adapter.originalTitles)
        }
    }
}