package com.example.tobuy.arch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tobuy.database.AppDatabase
import com.example.tobuy.database.entity.ItemEntity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ToBuyViewModel(): ViewModel() {

    private lateinit var repository : ToBuyRepository

    val itemEntityLiveData = MutableLiveData<List<ItemEntity>>()

    val transactionCompleteLiveData = MutableLiveData<Boolean>()

    //this needs to be called before doing anything with the viewmodel
    fun init(appDatabase: AppDatabase){
        repository = ToBuyRepository(appDatabase)

        viewModelScope.launch {
            repository.getAllItems().collect { items ->
                itemEntityLiveData.postValue(items)
            }
        }
    }

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
}