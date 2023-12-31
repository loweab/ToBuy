package com.example.tobuy.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import com.example.tobuy.arch.ToBuyViewModel
import com.example.tobuy.database.AppDatabase

abstract class BaseFragment: Fragment() {

    protected val mainActivity
        get() = activity as MainActivity

    protected val appDatabase
        get() = AppDatabase.getDatabase(requireActivity())

    //scoped to the activity, will not be destroyed when fragment is destroyed
    protected val sharedViewModel: ToBuyViewModel by activityViewModels()

    // region Navigation helper methods
    protected fun navigateUp() {
        mainActivity.navController.navigateUp()
    }

    protected fun navigateViaNavGraph(actionId: Int) {
        mainActivity.navController.navigate(actionId)
    }

    protected fun navigateViaNavGraph(navDirections: NavDirections) {
        mainActivity.navController.navigate(navDirections)
    }
    // endregion Navigation helper methods
}