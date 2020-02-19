package com.example.recipeapplication.addedit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapplication.entity.Recipe
import com.example.recipeapplication.room.RecipeDatabase
import com.example.recipeapplication.room.Repository
import kotlinx.coroutines.launch

class AddEditViewModel (application: Application): AndroidViewModel(application) {
    var recipe =  MutableLiveData<Recipe>()
    private var repository: Repository
    init {
        val recipeDao = RecipeDatabase.getDatabase(
            application,
            viewModelScope
        ).recipeDAO()
        repository = Repository(recipeDao)
    }

    fun insert(recipe: Recipe)  = viewModelScope.launch {
        repository.insert(recipe)
    }

    fun update(recipe: Recipe)  = viewModelScope.launch {
        repository.update(recipe)
    }

    fun delete(id: Int)  = viewModelScope.launch {
        repository.delete(id)
    }

}