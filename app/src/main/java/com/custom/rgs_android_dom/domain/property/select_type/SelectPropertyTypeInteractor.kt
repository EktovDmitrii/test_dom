package com.custom.rgs_android_dom.domain.property.select_type

import com.custom.rgs_android_dom.domain.property.select_type.view_states.SelectPropertyTypeViewState
import io.reactivex.subjects.PublishSubject

class SelectPropertyTypeInteractor() {

    val selectPropertyTypeViewStateSubject = PublishSubject.create<SelectPropertyTypeViewState>()

    private var selectPropertyTypeViewState = SelectPropertyTypeViewState(
        isSelectHomeLinearLayoutSelected = false,
        isSelectAppartmentLinearLayoutSelected = false,
        isNextTextViewEnabled = false
    )

    fun onSelectHomeClick(){
        selectPropertyTypeViewState = selectPropertyTypeViewState.copy(
            isSelectHomeLinearLayoutSelected = true,
            isSelectAppartmentLinearLayoutSelected = false,
            isNextTextViewEnabled = true
        )
        selectPropertyTypeViewStateSubject.onNext(selectPropertyTypeViewState)
    }

    fun onSelectAppartmentClick(){
        selectPropertyTypeViewState = selectPropertyTypeViewState.copy(
            isSelectHomeLinearLayoutSelected = false,
            isSelectAppartmentLinearLayoutSelected = true,
            isNextTextViewEnabled = true
        )
        selectPropertyTypeViewStateSubject.onNext(selectPropertyTypeViewState)
    }
}