package com.example.kolokvijumskiapp.cats.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kolokvijumskiapp.cats.api.models.CatsApiModel
import com.example.kolokvijumskiapp.cats.repository.CatsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.kolokvijumskiapp.cats.list.CatsListContract.CatsListState
import com.example.kolokvijumskiapp.cats.list.CatsListContract.CatsListUiEvent
import com.example.kolokvijumskiapp.cats.list.model.CatsUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class CatsListViewModel(
    private val repository: CatsRepository = CatsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(CatsListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: CatsListState.() -> CatsListState) = _state.update(reducer)//TODO check what does vezbe 5


    private val events = MutableSharedFlow<CatsListUiEvent>()
    fun setEvent(event: CatsListUiEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        fetchAllCats()
    }

    private fun observeEvents(){
        viewModelScope.launch {
            events
                .filterIsInstance<CatsListUiEvent.SearchQueryChanged>()
                .debounce(2.seconds)
                .collect{
                when(it){

                    is CatsListUiEvent.SearchQueryChanged ->{
                        //TODO
                        viewModelScope.launch {
                            setState { copy(loading = true) }
                            Log.d("heyhey", "2")
                            if(it.query.trim() != "") {
                                try {
                                    val cats = withContext(Dispatchers.IO) {
                                        repository.fetchAllFilteredCats(query = it.query)
                                            .map { it.asCatsUiModel() }
                                    }
                                    Log.d("heyhey", "3")
                                    setState {
                                        copy(cats = cats)
                                    }
                                } catch (error: Exception) {
                                    Log.d("heyhey", error.toString())
                                } finally {
                                    Log.d("heyhey", "4")
                                    setState { copy(loading = false) }
                                }
                            }else fetchAllCats()
                        }
                    }
                }
            }

            events
                .filterIsInstance<CatsListUiEvent.ClearSearch>()
                .collect{

            }
            events
                .filterIsInstance<CatsListUiEvent.CloseSearchMode>()
                .collect{
            }
        }
    }

    private fun fetchAllCats(){
        Log.d("heyhey", "1")
        viewModelScope.launch {
            setState { copy(loading = true) }
            Log.d("heyhey", "2")
            try {
                val cats = withContext(Dispatchers.IO) {
                    repository.fetchAllCats().map { it.asCatsUiModel() }
                }
                Log.d("heyhey", "3")
                setState { copy(cats = cats)
                }
            }catch (error: Exception){
                Log.d("heyhey", error.toString())
            }finally {
                Log.d("heyhey", "4")
                setState { copy(loading = false) }
            }
        }
    }


    private fun CatsApiModel.asCatsUiModel() = CatsUiModel(
        id = this.id,
        name = this.name,
        alt_names = this.alt_names,
        description = this.description,
        temperament = this.temperament,
    )


}