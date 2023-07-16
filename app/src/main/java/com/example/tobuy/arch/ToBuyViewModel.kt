package com.example.tobuy.arch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tobuy.database.AppDatabase
import com.example.tobuy.database.entity.ItemEntity
import kotlinx.coroutines.launch

class ToBuyViewModel(): ViewModel() {

    private lateinit var repository : ToBuyRepository

    val itemEntityLiveData = MutableLiveData<List<ItemEntity>>()

    //this needs to be called before doing anything with the viewmodel
    fun init(appDatabase: AppDatabase){
        repository = ToBuyRepository(appDatabase)

        viewModelScope.launch {
            val items = repository.getAllItems()
            itemEntityLiveData.postValue(items)
        }
    }

    fun insertItem(itemEntity: ItemEntity){
        viewModelScope.launch {
            repository.insertItem(itemEntity)
        }

    }

    fun deleteItem(itemEntity: ItemEntity){
        viewModelScope.launch {
            repository.deleteItem(itemEntity)
        }
    }
}