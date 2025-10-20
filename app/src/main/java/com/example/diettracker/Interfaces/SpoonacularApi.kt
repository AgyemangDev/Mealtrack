package com.example.diettracker.Interfaces
import com.example.diettracker.models.RecipeResponse

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// API Interface
interface SpoonacularApi {
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("number") number: Int = 10,
        @Query("addRecipeNutrition") addNutrition: Boolean = true,
        @Query("apiKey") apiKey: String
    ): RecipeResponse
}

// Retrofit instance
object ApiClient {
    val api: SpoonacularApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpoonacularApi::class.java)
    }
}
