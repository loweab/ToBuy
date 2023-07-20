package com.example.tobuy.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tobuy.R
import com.example.tobuy.databinding.FragmentProfileBinding
import com.example.tobuy.ui.BaseFragment


class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileEpoxyController = ProfileEpoxyController(
        onCategoryEmptyStateClicked = ::onCategoryEmptyStateClicked
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.epoxyRecyclerView.setController(profileEpoxyController)

        sharedViewModel.categoryEntityLiveData.observe(viewLifecycleOwner){categoryEntityList ->
            profileEpoxyController.categories = categoryEntityList
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity.hideKeyboard(requireView())
    }

    private fun onCategoryEmptyStateClicked(){
        navigateViaNavGraph(R.id.action_profileFragment_to_addCategoryFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}