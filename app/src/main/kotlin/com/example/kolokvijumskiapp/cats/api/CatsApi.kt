package com.example.kolokvijumskiapp.cats.api

import com.example.kolokvijumskiapp.cats.api.models.CatsApiModel
import com.example.kolokvijumskiapp.cats.api.models.image
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatsApi {

    @GET("breeds")
    suspend fun getAllCats(): List<CatsApiModel>

    @GET("breeds/{id}")
    suspend fun getCat(
        @Path("id") id: String,
    ): CatsApiModel

    @GET("breeds/search")
    suspend fun getFilteredCats(
        @Query("q") query: String,
    ): List<CatsApiModel>

    @GET("images/{id}")
    suspend fun getImage(
        @Path("id") id: String,
    ): image
}