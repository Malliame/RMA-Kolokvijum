package com.example.kolokvijumskiapp.cats.details

import com.example.kolokvijumskiapp.cats.api.models.CatsApiModel
import com.example.kolokvijumskiapp.cats.api.models.image
import com.example.kolokvijumskiapp.cats.api.models.weight

interface CatsDetailsContract {

    data class CatDetailsState(
        private val weight: weight = weight("", ""),
        private val image: image = image("", 0, 0, ""),
        val fetching: Boolean = false,
        val fetchinImage: Boolean = false,
        val cat: CatsApiModel = CatsApiModel("","","","","","","",
            0,0,0,0,0,0,0,0,0,0,0,0,"", image,weight, ":"),

        )
//    sealed class CatDetailsUiEvent {
//
//    }

}