package com.example.kolokvijumskiapp.cats.api.models

import kotlinx.serialization.Serializable

@Serializable
data class CatsApiModel(
    val id: String,
    val name: String,
    val temperament: String,
    val origin: String,
    val description: String,
    val life_span: String,
    val alt_names: String = "",
    val adaptability: Int,
    val affection_level: Int,
    val child_friendly: Int,
    val energy_level: Int,
    val grooming: Int,
    val health_issues: Int,
    val intelligence: Int,
    val shedding_level: Int,
    val social_needs: Int,
    val stranger_friendly: Int,
    val vocalisation: Int,
    val rare: Int,
    val wikipedia_url: String = "",
    val image: image = image( "",0, 0,""),
    val weight: weight,
    val reference_image_id: String = "",
)

@Serializable
data class weight(
    val imperial: String,
    val metric: String
)

@Serializable
data class image(
    val id: String,
    val width: Int,
    val height: Int,
    val url: String
)