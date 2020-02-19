package com.example.recipeapplication.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.recipeapplication.entity.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Recipe::class), version = 1, exportSchema = false)
abstract class RecipeDatabase: RoomDatabase() {
    abstract fun recipeDAO(): RecipeDAO

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var recipeDao = database.recipeDAO()

//                    // Delete all content here.
//                    recipeDao.deleteAll()
//
//                    // Add sample words.
//                    var recipe = Recipe(1, "Name1", "123", "fry", "123")
//                    recipeDao.insert(recipe)
//                    recipe = Recipe(2, "Name2", "123", "boil", "123")
//                    recipeDao.insert(recipe)
//                    recipe = Recipe(3, "Name3", "123", "steam", "123")
//                    recipeDao.insert(recipe)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE : RecipeDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope): RecipeDatabase {
            val tmpInstance = INSTANCE
            if(tmpInstance !== null){
                return tmpInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                ).addCallback(
                    WordDatabaseCallback(
                        scope
                    )
                ).build()
                INSTANCE = instance
                return instance
            }

        }

    }
}