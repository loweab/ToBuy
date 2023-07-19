package com.example.tobuy.database.entity

import com.example.tobuy.database.entity.ItemEntity

interface ItemEntityInterface {
    fun onItemSelected(itemEntity: ItemEntity)
    fun onBumpPriority(itemEntity: ItemEntity)
}