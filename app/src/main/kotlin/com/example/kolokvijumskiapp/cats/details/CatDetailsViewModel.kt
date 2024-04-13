package com.example.kolokvijumskiapp.cats.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kolokvijumskiapp.cats.api.models.CatsApiModel
import com.example.kolokvijumskiapp.cats.api.models.weight
import com.example.kolokvijumskiapp.cats.api.models.image
import com.example.kolokvijumskiapp.cats.details.CatsDetailsContract.CatDetailsState
import com.example.kolokvijumskiapp.cats.repository.CatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class CatDetailsViewModel (
    private val catid: String,
    private val repository: CatsRepository = CatsRepository
) : ViewModel(){

    private val _state = MutableStateFlow(CatDetailsState())
    val state = _state.asStateFlow()
    private fun setState(reducer: CatDetailsState.() -> CatDetailsState) = _state.update(reducer)

    init {
        fetchCatDetails()
    }

    private fun fetchCatDetails(){
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                val cat = withContext(Dispatchers.IO){
                    repository.fetchCat(id = catid)
                }
                setState { copy(cat = cat) }
                fetchCatImages()
            }catch (error: Exception){

            }finally {
                setState { copy(fetching = false) }
            }

        }
    }


    private fun fetchCatImages() {
        viewModelScope.launch {
            setState { copy(fetchinImage = true) }
            var catImageUrl = try{
                withContext(Dispatchers.IO){
                    repository.fetchImage(imageId = _state.value.cat.reference_image_id)
                }
            }catch (error: Exception) {
                null
            }

            if(catImageUrl == null) catImageUrl = image("",0,0,"")

            setState { copy(cat = cat.copy(image = catImageUrl), fetchinImage = false) }

        }
    }



}