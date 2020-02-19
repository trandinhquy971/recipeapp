package com.example.recipeapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapplication.databinding.RecipeItemBinding
import com.example.recipeapplication.entity.Recipe

class RecipeAdapter: RecyclerView.Adapter<RecipeAdapter.ItemAdapterVH>() {
    var list: List<Recipe> = listOf()
    lateinit var listener: OnItemClickListener

    fun setItemList(list: List<Recipe>){
        this.list = list
    }

    fun setOnClickItemListener(listener: OnItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapterVH {
        val itemBinding: RecipeItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recipe_item, parent,
            false)
        return ItemAdapterVH(itemBinding.root, itemBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemAdapterVH, position: Int) {
        var thisItem = list[position]
        holder.itemBinding.recipe = thisItem
        holder.itemView.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                listener.onItemClickListener(thisItem)
            }
        })
    }

    inner class ItemAdapterVH(itemView: View, itemBinding: RecipeItemBinding): RecyclerView.ViewHolder(itemView) {
        var itemBinding: RecipeItemBinding = itemBinding
    }
}

interface OnItemClickListener{
    fun onItemClickListener(recipe: Recipe)
}