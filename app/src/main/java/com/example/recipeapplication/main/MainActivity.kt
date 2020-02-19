package com.example.recipeapplication.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.view.View
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.recipeapplication.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import com.example.recipeapplication.*
import com.example.recipeapplication.addedit.AddEditActivity
import com.example.recipeapplication.entity.Recipe


class MainActivity : AppCompatActivity() {

    private var recipeTypes = arrayListOf<String>()
    private lateinit var viewModel: MainViewModel

    companion object {
        const val EDIT_CODE = 111
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Recipe App"

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        var listRecipeAdapter = RecipeAdapter()
        rv_recipe.adapter = listRecipeAdapter
        viewModel.notifyCurrencyList.observe(this, Observer {
            if(it != null) {
                listRecipeAdapter.list = it
                listRecipeAdapter.notifyDataSetChanged()
            }
        })
        listRecipeAdapter.setOnClickItemListener(object :
            OnItemClickListener {
            override fun onItemClickListener(recipe: Recipe) {
                startActivityForResult(
                    AddEditActivity.prepareIntent(
                        this@MainActivity,
                        recipe
                    ), EDIT_CODE
                )
            }
        })

        fab.setOnClickListener{
            this.startActivityForResult(Intent(this, AddEditActivity::class.java ),
                EDIT_CODE
            )
        }
        setUpRecipeTypes()
    }

    private fun setUpRecipeTypes(){
        recipeTypes.add("all")
        recipeTypes.addAll(getRecipeTypes())

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, recipeTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        spinner_recipe_type.adapter = adapter
        spinner_recipe_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("App","nothing selected")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("App", position.toString())
                if(recipeTypes[position] == "all"){
                    viewModel.getAllAphabetizedRecipes()
                } else {
                    viewModel.getAllAphabetizedWithFilter(recipeTypes[position])
                }
            }

        }
    }

    private fun getRecipeTypes(): ArrayList<String> {
        return resources.getStringArray(R.array.recipe_types).toCollection(ArrayList())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === EDIT_CODE) {
            if (resultCode === Activity.RESULT_OK) {
                if (spinner_recipe_type.selectedItem.toString() == "all")
                    viewModel.getAllAphabetizedRecipes()
                else {
                    viewModel.getAllAphabetizedWithFilter(spinner_recipe_type.selectedItem.toString())
                }
            }
        }
    }
}
