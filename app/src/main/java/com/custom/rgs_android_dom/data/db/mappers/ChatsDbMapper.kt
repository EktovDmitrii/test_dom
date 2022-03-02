package com.custom.rgs_android_dom.data.db.mappers

import com.custom.rgs_android_dom.BuildConfig
import com.custom.rgs_android_dom.data.db.models.chat.CaseDbModel
import com.custom.rgs_android_dom.data.db.models.chat.SubtypeDbModel
import com.custom.rgs_android_dom.data.network.responses.CaseResponse
import com.custom.rgs_android_dom.data.network.responses.ClientCasesResponse
import com.custom.rgs_android_dom.data.network.responses.SubtypeResponse
import com.custom.rgs_android_dom.domain.chat.models.*
import com.custom.rgs_android_dom.utils.asEnumOrDefault
import org.joda.time.DateTime

object ChatsDbMapper {

    fun fromResponse(
        response: ClientCasesResponse,
        subtypes: List<SubtypeResponse>,
        masterOnlineChannelId: String,
        masterOnlineUnreadPosts: Int
    ): List<CaseDbModel> {
        val cases = arrayListOf<CaseDbModel>().apply {
            add(
                CaseDbModel(
                    channelId = masterOnlineChannelId,
                    name = "Онлайн мастер",
                    subtype = null,
                    taskId = "",
                    unreadPosts = masterOnlineUnreadPosts,
                    isArchived = false,
                    status = CaseStatus.UNKNOWN,
                    subStatus = CaseSubStatus.UNKNOWN,
                    reportedAt = DateTime.now()
                )
            )
            if (response.activeCases != null){
                addAll(response.activeCases.map { fromCaseResponse(it, subtypes, false) })
            }
        }
        cases.addAll(response.archivedCases?.map { fromCaseResponse(it, subtypes, true) } ?: listOf())
        return cases
    }

    fun toModel(cases: List<CaseDbModel>): ClientCasesModel {
        return ClientCasesModel(
            activeCases = cases.filter { !it.isArchived } .map { fromCaseDbModel(it) },
            archivedCases = cases.filter { it.isArchived }.map { fromCaseDbModel(it) }
        )
    }


    private fun fromCaseResponse(response: CaseResponse, subtypes: List<SubtypeResponse>, isArchived: Boolean): CaseDbModel {
        val subtype = subtypes.find { it.subtype == response.subtype }
        return CaseDbModel(
            channelId = response.channelId,
            name = response.name,
            subtype = subtype?.let { fromSubtypeResponse(it) },
            taskId = response.taskId,
            unreadPosts = response.unreadPosts,
            isArchived = isArchived,
            status = response.status.asEnumOrDefault(CaseStatus.UNKNOWN),
            subStatus = response.subStatus.asEnumOrDefault(CaseSubStatus.UNKNOWN),
            reportedAt = response.reportedAt
        )
    }

    private fun fromSubtypeResponse(response: SubtypeResponse): SubtypeDbModel {
        return SubtypeDbModel(
            archivedAt = response.archivedAt,
            internal = response.internal,
            logo = response.logo,
            name = response.name,
            subtype = response.subtype,
            type = response.type
        )
    }

    private fun fromCaseDbModel(caseDbModel: CaseDbModel): CaseModel {
        return CaseModel(
            channelId = caseDbModel.channelId,
            name = caseDbModel.name,
            subtype = caseDbModel.subtype?.let { fromSubtypeDbModel(it) },
            taskId = caseDbModel.taskId,
            unreadPosts = caseDbModel.unreadPosts,
            isArchived = caseDbModel.isArchived,
            status = caseDbModel.status,
            subStatus = caseDbModel.subStatus,
            reportedAt = caseDbModel.reportedAt
        )
    }

    private fun fromSubtypeDbModel(subtypeDbModel: SubtypeDbModel): SubtypeModel {
        return SubtypeModel(
            archivedAt = subtypeDbModel.archivedAt,
            internal = subtypeDbModel.internal,
            logo = "${BuildConfig.BASE_URL}/api/store/${subtypeDbModel.logo}",
            name = subtypeDbModel.name,
            subtype = subtypeDbModel.subtype,
            type = subtypeDbModel.type
        )
    }

}