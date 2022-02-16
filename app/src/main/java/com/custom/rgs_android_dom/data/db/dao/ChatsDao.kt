package com.custom.rgs_android_dom.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.custom.rgs_android_dom.data.db.models.chat.CaseDbModel
import io.reactivex.Flowable

@Dao
interface ChatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCases(cases: List<CaseDbModel>)

    @Query("SELECT * FROM cases")
    fun getCasesFlowable(): Flowable<List<CaseDbModel>>
}