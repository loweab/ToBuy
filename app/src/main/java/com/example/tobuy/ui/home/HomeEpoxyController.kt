package com.example.tobuy.ui.home

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.R
import com.example.tobuy.addHeaderModel
import com.example.tobuy.database.entity.ItemEntity
import com.example.tobuy.database.entity.ItemEntityInterface
import com.example.tobuy.databinding.ModelEmptyStateBinding
import com.example.tobuy.databinding.ModelHeaderItemBinding
import com.example.tobuy.databinding.ModelItemEntityBinding
import com.example.tobuy.ui.epoxy.LoadingEpoxyModel
import com.example.tobuy.ui.epoxy.ViewBindingKotlinModel
import com.example.tobuy.ui.epoxy.models.HeaderEpoxyModel

class HomeEpoxyController(private val itemEntityInterface: ItemEntityInterface): EpoxyController() {

    var isLoading: Boolean = true
        set(value) {
            field = value
            if(field){
                requestModelBuild()
            }
        }

    var itemEntityList = ArrayList<ItemEntity>()
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }

    override fun buildModels() {
        if(isLoading){
            LoadingEpoxyModel().id("loading_state").addTo(this)
            return
        }

        if(itemEntityList.isEmpty()){
            EmptyStateEpoxyModel().id("empty_state").addTo(this)
            return
        }

        var currentPriority: Int = -1
        itemEntityList.sortedByDescending {
            it.priority
        }.forEach { item ->
            if(item.priority != currentPriority){
                currentPriority = item.priority
                addHeaderModel(getHeaderTextFromPriority(currentPriority))
            }
            ItemEntityEpoxyModel(item, itemEntityInterface).id(item.id).addTo(this)
        }
    }

    private fun getHeaderTextFromPriority(priority: Int): String {
        return when(priority){
            1 -> "Low"
            2 -> "Medium"
            else -> "High"
        }
    }

    data class ItemEntityEpoxyModel(val itemEntity: ItemEntity, val itmEntityInterface: ItemEntityInterface):
        ViewBindingKotlinModel<ModelItemEntityBinding>(R.layout.model_item_entity){

        override fun ModelItemEntityBinding.bind() {
            titleTextView.text = itemEntity.title

            if(itemEntity.description == null){
                descriptionTextView.isGone = true
            }else{
                descriptionTextView.isVisible = true
                descriptionTextView.text = itemEntity.description
            }

            priorityTextView.setOnClickListener {
                itmEntityInterface.onBumpPriority(itemEntity)
            }

            val colorRes = when(itemEntity.priority){
                1 -> android.R.color.holo_green_dark
                2 -> android.R.color.holo_orange_dark
                3 -> android.R.color.holo_red_dark
                else -> R.color.gray_700
            }

            val color = ContextCompat.getColor(root.context, colorRes)
            priorityTextView.setBackgroundColor(color)
            cardView.setStrokeColor(ColorStateList.valueOf(color))

            root.setOnClickListener {
                itmEntityInterface.onItemSelected(itemEntity)
            }
        }
    }

    class EmptyStateEpoxyModel : ViewBindingKotlinModel<ModelEmptyStateBinding>(R.layout.model_empty_state){
        override fun ModelEmptyStateBinding.bind() {
            //nothing right now
        }
    }


}