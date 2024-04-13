package com.example.kolokvijumskiapp.cats.repository

import android.util.Log
import com.example.kolokvijumskiapp.cats.api.CatsApi
import com.example.kolokvijumskiapp.cats.api.models.CatsApiModel
import com.example.kolokvijumskiapp.cats.api.models.image
import com.example.kolokvijumskiapp.networking.retrofit

object CatsRepository {

    private val catsApi: CatsApi = retrofit.create(CatsApi::class.java)

    suspend fun fetchAllCats(): List<CatsApiModel>{
        val cats = catsApi.getAllCats()
        return  cats
    }

    suspend fun fetchAllFilteredCats(query: String): List<CatsApiModel>{
        val cats = catsApi.getFilteredCats(query)

        return  cats
    }

    suspend fun fetchImage(imageId: String): image{
        val image = catsApi.getImage(imageId)
        return image
    }

    suspend fun fetchCat(id: String): CatsApiModel{
        val cat = catsApi.getCat(id)

        return cat
    }
}