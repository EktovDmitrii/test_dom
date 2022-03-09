package com.custom.rgs_android_dom.domain.chat.models

data class ClientCasesModel(
    val activeCases: List<CaseModel>,
    val archivedCases: List<CaseModel>
)