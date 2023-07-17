package com.example.tobuy.database.entity

import com.example.tobuy.database.entity.ItemEntity

interface ItemEntityInterface {

    fun onBumpPriority(itemEntity: ItemEntity)
}