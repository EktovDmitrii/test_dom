package com.custom.rgs_android_dom.data.db.converters

import androidx.room.TypeConverter
import com.custom.rgs_android_dom.data.db.models.chat.CaseDbModel
import com.google.gson.Gson

object CaseConverter {

    @TypeConverter
    fun fromCase(case: CaseDbModel): String {
        return Gson().toJson(case)
    }

    @TypeConverter
    fun toCase(caseStr: String): CaseDbModel{
        return Gson().fromJson(caseStr, CaseDbModel::class.java)
    }

}