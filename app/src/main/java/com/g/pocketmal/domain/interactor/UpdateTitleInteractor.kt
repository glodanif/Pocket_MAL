package com.g.pocketmal.domain.interactor

import com.g.pocketmal.data.api.ApiService
import com.g.pocketmal.data.api.NetworkException
import com.g.pocketmal.data.api.PostParamsFactory
import com.g.pocketmal.data.api.UpdateParams
import com.g.pocketmal.data.database.datasource.RecordDataSource
import com.g.pocketmal.data.database.model.DbListRecord
import com.g.pocketmal.data.util.TitleType
import com.g.pocketmal.data.common.Status

class UpdateTitleInteractor(
        private val apiService: ApiService,
        private val recordStorage: RecordDataSource
) : BaseInteractor<UpdateTitleInteractor.Params, DbListRecord>() {

    override suspend fun execute(input: Params): DbListRecord {

        val paramsMap = PostParamsFactory
                .getParamsFromUpdateData(input.updateParams, input.titleType)
        val response =
                apiService.updateTitle(input.recordId, paramsMap, input.titleType)

        if (response.isSuccessful) {
            val old = recordStorage.getRecordById(input.recordId, input.titleType)
            if (old != null) {
                applyChangesToDbModel(old, input.updateParams)
                recordStorage.saveRecord(old)
                return old
            } else {
                throw IllegalStateException("Title was not found in local db id=${input.recordId} (${input.titleType})")
            }
        } else {
            throw NetworkException(response.code(), errorMessage = "Updation request was not successful")
        }
    }

    private fun applyChangesToDbModel(dbRecord: DbListRecord, params: UpdateParams) {

        if (params.episodes != null && dbRecord.myEpisodes != params.episodes) {
            dbRecord.myLastUpdated = System.currentTimeMillis()
        }
        params.episodes?.let {
            dbRecord.myEpisodes = it
        }
        params.chapters?.let {
            dbRecord.myEpisodes = it
        }
        params.volumes?.let {
            dbRecord.mySubEpisodes = it
        }
        params.status?.let {
            dbRecord.myStatus = Status.from(it)
        }
        params.score?.let {
            dbRecord.myScore = it
        }
        params.tags?.let {
            dbRecord.myTags = it
        }
        params.startDate?.let {
            dbRecord.myStartDate = it
        }
        params.finishDate?.let {
            dbRecord.myFinishDate = it
        }
        params.reWatching?.let {
            dbRecord.myRe = it
        }
        params.reReading?.let {
            dbRecord.myRe = it
        }
        params.reWatchedTimes?.let {
            dbRecord.myReTimes = it
        }
        params.reWatchValue?.let {
            dbRecord.myReTimes = it
        }
        params.reReadTimes?.let {
            dbRecord.myReTimes = it
        }
        params.reReadValue?.let {
            dbRecord.myReValue = it
        }
        params.comments?.let {
            dbRecord.myComments = it
        }
        params.priority?.let {
            dbRecord.myPriority = it
        }
    }

    class Params(
            val recordId: Int,
            val titleType: TitleType,
            val updateParams: UpdateParams
    )
}