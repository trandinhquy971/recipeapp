package com.example.recipeapplication.main

import android.app.Application
import androidx.lifecycle.*
import com.example.recipeapplication.entity.Recipe
import com.example.recipeapplication.room.RecipeDatabase
import com.example.recipeapplication.room.Repository
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private var repository: Repository
//    var allRecipes: LiveData<List<Recipe>>
    val notifyCurrencyList = MediatorLiveData<List<Recipe>>()

    init {
        val recipeDao = RecipeDatabase.getDatabase(
            application,
            viewModelScope
        ).recipeDAO()
        repository = Repository(recipeDao)
//        allRecipes = repository.notifyCurrencyList
    }

    fun insert(recipe: Recipe) = viewModelScope.launch {
        repository.insert(recipe)
    }

    fun getAllAphabetizedRecipes() = viewModelScope.launch {
        notifyCurrencyList.addSource(repository.getAlphabetizedRecipes()) {
            notifyCurrencyList.value = it
        }
//        repository.getAlphabetizedRecipes()
    }

    fun getAllAphabetizedWithFilter(type: String) = viewModelScope.launch {
        notifyCurrencyList.addSource(repository.getAlphabetizedRecipesWithFilter(type)) {
            notifyCurrencyList.value = it
        }
//        repository.getAlphabetizedRecipesWithFilter(type)
    }
}