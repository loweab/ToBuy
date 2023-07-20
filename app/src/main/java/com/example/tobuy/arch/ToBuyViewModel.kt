package com.example.tobuy.arch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tobuy.database.AppDatabase
import com.example.tobuy.database.entity.CategoryEntity
import com.example.tobuy.database.entity.ItemEntity
import kotlinx.coroutines.launch

class ToBuyViewModel(): ViewModel() {

    private lateinit var repository : ToBuyRepository

    val itemEntityLiveData = MutableLiveData<List<ItemEntity>>()
    val categoryEntityLiveData = MutableLiveData<List<CategoryEntity>>()

    val transactionCompleteLiveData = MutableLiveData<Boolean>()

    //this needs to be called before doing anything with the viewmodel
    fun init(appDatabase: AppDatabase){
        repository = ToBuyRepository(appDatabase)

        viewModelScope.launch {
            repository.getAllItems().collect { items ->
                itemEntityLiveData.postValue(items)
            }
        }

        viewModelScope.launch {
            repository.getAllCategories().collect { categories ->
                categoryEntityLiveData.postValue(categories)
            }
        }
    }
    //region ItemEntity
    fun insertItem(itemEntity: ItemEntity){
        viewModelScope.launch {
            repository.insertItem(itemEntity)

            transactionCompleteLiveData.postValue(true)
        }

    }

    fun deleteItem(itemEntity: ItemEntity){
        viewModelScope.launch {
            repository.deleteItem(itemEntity)
        }
    }

    fun updateItem(itemEntity: ItemEntity){
        viewModelScope.launch {
            repository.updateItem(itemEntity)
            transactionCompleteLiveData.postValue(true)
        }
    }
    //endregion ItemEntity

    //region CategoryEntity
    fun insertCategory(categoryEntity: CategoryEntity){
        viewModelScope.launch {
            repository.insertCategory(categoryEntity)

            transactionCompleteLiveData.postValue(true)
        }

    }

    fun deleteCategory(categoryEntity: CategoryEntity){
        viewModelScope.launch {
            repository.deleteCategory(categoryEntity)
        }
    }

    fun updateCategory(categoryEntity: CategoryEntity){
        viewModelScope.launch {
            repository.updateCategory(categoryEntity)
            transactionCompleteLiveData.postValue(true)
        }
    }
    //endregion CategoryEntity
}