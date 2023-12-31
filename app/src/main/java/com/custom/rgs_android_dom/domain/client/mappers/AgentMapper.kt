package com.custom.rgs_android_dom.domain.client.mappers

import com.custom.rgs_android_dom.data.network.responses.RequestEditAgentTasksResponse
import com.custom.rgs_android_dom.domain.client.models.ClientModel
import com.custom.rgs_android_dom.domain.client.models.RequestEditAgentStatus
import com.custom.rgs_android_dom.domain.client.models.RequestEditAgentTaskModel
import com.custom.rgs_android_dom.domain.client.view_states.AgentViewState
import com.custom.rgs_android_dom.utils.PhoneMaskHelper
import com.custom.rgs_android_dom.utils.formatPhoneByMask

object AgentMapper {

    fun from(client: ClientModel): AgentViewState {
        val phoneMask = PhoneMaskHelper.getMaskForPhone(client.agent?.phone ?: "")
        val phone = client.agent?.phone?.formatPhoneByMask(phoneMask, "#") ?: ""
        return AgentViewState(
            isEditAgentButtonVisible = phone.isEmpty(),
            agentPhone = phone,
            agentCode = client.agent?.code ?: ""
        )
    }

    fun responseToRequestEditAgentTaskModel(request: RequestEditAgentTasksResponse): List<RequestEditAgentTaskModel>{
        return request.tasks?.map {task->
            RequestEditAgentTaskModel(
                status = if (task.status == "open") RequestEditAgentStatus.OPEN else RequestEditAgentStatus.UNSPECIFIED,
                subStatus = task.subStatus,
                taskId = task.taskId
            )
        } ?: emptyList()
    }

}