package com.example.recipeapplication.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "recipe_table")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int ?= null,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "step")
    var step: String,
    @ColumnInfo(name = "type")
    var type: String,
    @ColumnInfo(name = "ingredient")
    var ingredients: String,
    @ColumnInfo(name = "img")
    var img: String
): Serializable
