package com.g.pocketmal.data.api

import com.g.pocketmal.data.DataTitleType

object PostParamsFactory {

    fun getParamsFromUpdateData(params: UpdateParams, titleType: DataTitleType): HashMap<String, Any> {

        val paramsMap = HashMap<String, Any>()

        params.episodes?.let {
            paramsMap["num_watched_episodes"] = it
        }
        params.chapters?.let {
            paramsMap["num_chapters_read"] = it
        }
        params.volumes?.let {
            paramsMap["num_volumes_read"] = it
        }

        //FIXME
        params.status?.let {
            paramsMap["status"] = when {
                it == Status.IN_PROGRESS.status && titleType == DataTitleType.MANGA -> "reading"
                it == Status.PLANNED.status && titleType == DataTitleType.MANGA -> "plan_to_read"
                else -> it
            }
        }
        params.score?.let {
            paramsMap["score"] = it
        }
        params.tags?.let {
            paramsMap["tags"] = it
        }
        params.startDate?.let {
            paramsMap["start_date"] = it
        }
        params.finishDate?.let {
            paramsMap["finish_date"] = it
        }
        params.reWatching?.let {
            paramsMap["is_rewatching"] = it
        }
        params.reWatchedTimes?.let {
            paramsMap["num_times_rewatched"] = it
        }
        params.reWatchValue?.let {
            paramsMap["rewatch_value"] = it
        }
        params.reReading?.let {
            paramsMap["is_rereading"] = it
        }
        params.reReadTimes?.let {
            paramsMap["num_times_reread"] = it
        }
        params.reReadValue?.let {
            paramsMap["reread_value"] = it
        }
        params.comments?.let {
            paramsMap["comments"] = it
        }
        params.priority?.let {
            paramsMap["priority"] = it
        }
        return paramsMap
    }
}
