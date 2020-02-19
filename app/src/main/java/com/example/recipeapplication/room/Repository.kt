package com.example.recipeapplication.room

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.recipeapplication.entity.Recipe


class Repository(private val recipeDAO: RecipeDAO) {

    var allRecipes: LiveData<List<Recipe>> = recipeDAO.getAlphabetizedRecipes()
//    val allRecipes: LiveData<List<Recipe>> = MutableLiveData()
//
//    init {
//        allRecipes.postValue(recipeDAO.getAlphabetizedRecipes().value)
//    }

    suspend fun insert(recipe: Recipe){
        recipeDAO.insert(recipe)
    }

    suspend fun update(recipe: Recipe){
        recipeDAO.update(recipe)
    }

    suspend fun delete(id: Int){
        recipeDAO.delete(id)
    }

    fun getAlphabetizedRecipes(): LiveData<List<Recipe>>{
        return recipeDAO.getAlphabetizedRecipes()
    }

    fun getAlphabetizedRecipesWithFilter(type: String): LiveData<List<Recipe>>{
        return recipeDAO.getAlphabetizedRecipesWithFilter(type)
        Log.d("App", "asdasdas")
    }
}