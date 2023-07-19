package com.example.tobuy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyTouchHelper
import com.example.tobuy.R
import com.example.tobuy.database.entity.ItemEntity
import com.example.tobuy.database.entity.ItemEntityInterface
import com.example.tobuy.databinding.FragmentHomeBinding
import com.example.tobuy.ui.BaseFragment

class HomeFragment : BaseFragment(), ItemEntityInterface {

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            navigateViaNavGraph(R.id.action_homeFragment_to_addItemEntityFragment)
        }

        val controller = HomeEpoxyController(this)
        binding.epoxyRecycler.setController(controller)

        sharedViewModel.itemEntityLiveData.observe(viewLifecycleOwner){itemEntityList ->
            controller.itemEntityList = itemEntityList as ArrayList<ItemEntity>
        }

        EpoxyTouchHelper.initSwiping(binding.epoxyRecycler)
            .right()
            .withTarget(HomeEpoxyController.ItemEntityEpoxyModel::class.java)
            .andCallbacks(object : EpoxyTouchHelper.SwipeCallbacks<HomeEpoxyController.ItemEntityEpoxyModel>(){
                override fun onSwipeCompleted(
                    model: HomeEpoxyController.ItemEntityEpoxyModel?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    val itemThatWasRemoved = model?.itemEntity ?: return
                    sharedViewModel.deleteItem(itemThatWasRemoved)
                }

            })
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideKeyboard(requireView())
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onBumpPriority(itemEntity: ItemEntity) {
        val currentPriority = itemEntity.priority
        var newPriority = currentPriority + 1
        if(newPriority > 3){
            newPriority = 1
        }
        val updatedItemEntity = itemEntity.copy(priority = newPriority)
        sharedViewModel.updateItem(updatedItemEntity)
    }

    override fun onItemSelected(itemEntity: ItemEntity) {
        val navDirections = HomeFragmentDirections.actionHomeFragmentToAddItemEntityFragment(itemEntity.id)
        navigateViaNavGraph(navDirections)
    }


}