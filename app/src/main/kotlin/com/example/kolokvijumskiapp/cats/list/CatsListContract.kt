package com.example.kolokvijumskiapp.cats.list

import com.example.kolokvijumskiapp.cats.list.model.CatsUiModel

interface CatsListContract {

    data class CatsListState(
        val loading: Boolean = false,
        val query: String = "",
        val isSearchMode: Boolean = false,
        val cats: List<CatsUiModel> = emptyList(),
    )

    sealed class CatsListUiEvent {
        data class SearchQueryChanged(val query: String) : CatsListUiEvent()
        data object ClearSearch : CatsListUiEvent()
        data object CloseSearchMode : CatsListUiEvent()
    }
}