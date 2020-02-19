package com.example.recipeapplication.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.recipeapplication.entity.Recipe

@Dao
interface RecipeDAO {

    @Query("SELECT * from recipe_table ORDER BY name ASC")
    fun getAlphabetizedRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * from recipe_table WHERE type = :string ORDER BY name ASC ")
    fun getAlphabetizedRecipesWithFilter(string: String): LiveData<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recipe: Recipe)

    @Update
    suspend fun update(recipe: Recipe)

    @Query("DELETE FROM recipe_table WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM recipe_table")
    suspend fun deleteAll()
}