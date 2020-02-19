package com.example.recipeapplication.addedit

import android.Manifest
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.recipeapplication.databinding.ActivityAddEditBinding
import kotlinx.android.synthetic.main.activity_add_edit.*
import android.content.Intent
import android.widget.Toast
import com.bumptech.glide.Glide
import java.io.FileNotFoundException
import android.content.pm.PackageManager
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import java.io.File
import android.provider.MediaStore
import com.example.recipeapplication.R
import com.example.recipeapplication.entity.Recipe


class AddEditActivity : AppCompatActivity() {

    private lateinit var viewModel: AddEditViewModel
    private var recipeTypes = arrayListOf<String>()
    private var imagePath: String? = null

    companion object {
        const val RESULT_LOAD_IMG = 1997
        const val READ_STORAGE_PERMISSION_REQUEST_CODE = 2807
        const val INTENT_EDIT = "intent_edit_recipte"


        @JvmStatic
        fun prepareIntent(context: Context, recipe: Recipe): Intent  = Intent (context, AddEditActivity::class.java).apply {
            putExtra(INTENT_EDIT, recipe)
        }
    }

    val recipe: Recipe? by lazy {
        intent.getSerializableExtra(INTENT_EDIT) as Recipe?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        if (!checkPermissionForReadExtertalStorage())
            requestPermissionForReadExtertalStorage()

        viewModel = ViewModelProviders.of(this).get(AddEditViewModel::class.java)

        val binding: ActivityAddEditBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_add_edit
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        recipeTypes = getRecipeTypes()
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            recipeTypes
        )

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        spinner_type.adapter = adapter
        spinner_type.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("App","nothing selected")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }
        })

        if (recipe != null) {
            //edit
            title = "Edit"
            btn_delete.visibility = View.VISIBLE
            btn_delete.setOnClickListener{
                viewModel.delete(recipe!!.id!!)
                setResult(Activity.RESULT_OK,  Intent())
                finish()
            }
            btn_proceses.text = "Submit"
            edt_name.setText(recipe!!.name)
            edt_steps.setText(recipe!!.step)
            edt_ingredients.setText(recipe!!.ingredients)
            imv_choose.visibility = View.VISIBLE
            Glide.with(this).load(File(recipe!!.img)).into(imv_choose)
            spinner_type.setSelection(recipeTypes.indexOf(recipe!!.type))
            imagePath = recipe!!.img
        }
        else {
            //add
            title = "Add"
            btn_delete.visibility = View.GONE
            btn_proceses.text = "Add"
        }

        btn_choose_img.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent,
                RESULT_LOAD_IMG
            )
        }

        btn_proceses.setOnClickListener {
            //add
            if (recipe == null) {
                if (!TextUtils.isEmpty(edt_name.text.toString())
                    && !TextUtils.isEmpty(edt_steps.text.toString())
                    && !TextUtils.isEmpty(spinner_type.selectedItem.toString())
                    && !TextUtils.isEmpty(edt_ingredients.toString())
                    && !TextUtils.isEmpty(imagePath)
                ) {
                    viewModel.insert(
                        Recipe(
                            name = edt_name.text.toString(),
                            step = edt_steps.text.toString(),
                            type = spinner_type.selectedItem.toString(),
                            ingredients = edt_ingredients.text.toString(),
                            img = imagePath!!
                        )
                    )
                    setResult(Activity.RESULT_OK,  Intent())
                    finish()
                }
                else {
                    Toast.makeText(this, resources.getText(R.string.fulfill_alert), Toast.LENGTH_LONG).show()
                }
            }
            //edit
            else {
                if (!TextUtils.isEmpty(edt_name.text.toString())
                    && !TextUtils.isEmpty(edt_steps.text.toString())
                    && !TextUtils.isEmpty(spinner_type.selectedItem.toString())
                    && !TextUtils.isEmpty(edt_ingredients.toString())
                ) {
                    viewModel.update(
                        Recipe(
                            id = recipe!!.id,
                            name = edt_name.text.toString(),
                            step = edt_steps.text.toString(),
                            type = spinner_type.selectedItem.toString(),
                            ingredients = edt_ingredients.text.toString(),
                            img = imagePath!!
                        )
                    )
                    setResult(Activity.RESULT_OK,  Intent())
                    finish()
                }
                else {
                    Toast.makeText(this, resources.getText(R.string.fulfill_alert), Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun checkPermissionForReadExtertalStorage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = this.checkSelfPermission(READ_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    @Throws(Exception::class)
    fun requestPermissionForReadExtertalStorage() {
        try {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_REQUEST_CODE
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == READ_STORAGE_PERMISSION_REQUEST_CODE){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                finish()
            }
        }
    }

    override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            try {
                val imageUri = data!!.data
                imv_choose.visibility = View.VISIBLE

                getRealPathFromURI(imageUri!!).let {
                    Glide.with(this).load(File(it)).into(imv_choose)
                    imagePath = it
                }

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this@AddEditActivity, resources.getText(R.string.pick_image_alert), Toast.LENGTH_LONG).show()
        }
    }

    private fun getRecipeTypes(): ArrayList<String> {
        return resources.getStringArray(R.array.recipe_types).toCollection(ArrayList())
    }

    private fun getRealPathFromURI(contentURI: Uri): String {
        var result = ""
        val cursor = contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            contentURI.path?.let {
                result = it
            }

        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
}
