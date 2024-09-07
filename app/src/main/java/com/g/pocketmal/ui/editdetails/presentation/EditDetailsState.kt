package com.g.pocketmal.ui.editdetails.presentation

sealed class EditDetailsState {
    data object Loading : EditDetailsState()
    data object IncorrectInput : EditDetailsState()
    data object NotFound : EditDetailsState()
    data class RecordDetails(val details: RecordExtraDetailsViewEntity) : EditDetailsState()
}
