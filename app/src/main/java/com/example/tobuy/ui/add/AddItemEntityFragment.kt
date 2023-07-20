package com.example.tobuy.ui.add

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.tobuy.R
import com.example.tobuy.database.entity.ItemEntity
import com.example.tobuy.databinding.FragmentAddItemEntityBinding
import com.example.tobuy.ui.BaseFragment
import java.lang.Exception
import java.util.UUID

class AddItemEntityFragment : BaseFragment() {

    private var _binding: FragmentAddItemEntityBinding? = null
    private val binding get() = _binding!!

    private val safeArgs: AddItemEntityFragmentArgs by navArgs()
    private val selectedItemEntity: ItemEntity? by lazy {
        sharedViewModel.itemEntityLiveData.value?.find {
            it.id == safeArgs.selectedItemEntityId
        }
    }

    private var isInEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddItemEntityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            saveItemEntityToDatabase()
        }

        binding.quantitySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val currentText = binding.titleEditText.text.toString().trim()
                if (currentText.isEmpty()){
                    return
                }

                val endIndex = currentText.indexOf("[") - 1
                val newText = if (endIndex > 0){
                    "${currentText.substring(0, endIndex)} [$progress]"
                }else{
                    "$currentText [$progress]"
                }

                val sanitizedText = newText.replace("[1]", "")
                binding.titleEditText.setText(sanitizedText)
                binding.titleEditText.setSelection(sanitizedText.length)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        sharedViewModel.transactionCompleteLiveData.observe(viewLifecycleOwner){complete ->
            if(complete){
                Toast.makeText(requireActivity(), "Item Saved!", Toast.LENGTH_SHORT).show()

                if(isInEditMode){
                    navigateUp()
                    return@observe
                }

                binding.titleEditText.text = null
                binding.titleEditText.requestFocus()
                mainActivity.showKeyboard(binding.titleEditText)

                binding.descriptionEditTet.text = null
                binding.radioGroup.check(R.id.radioButtonLow)
            }
        }

        //default focus on title ET and keyboard open
        binding.titleEditText.requestFocus()
        mainActivity.showKeyboard(binding.titleEditText)

        //setup screen if we're in edit mode, .let-> code runs if item isn't null
        selectedItemEntity?.let {itemEntity ->
            isInEditMode = true

            binding.titleEditText.setText(itemEntity.title)
            binding.titleEditText.setSelection(itemEntity.title.length)
            binding.descriptionEditTet.setText(itemEntity.description)
            when (itemEntity.priority){
                1 -> binding.radioGroup.check(R.id.radioButtonLow)
                2 -> binding.radioGroup.check(R.id.radioButtonMedium)
                else -> binding.radioGroup.check(R.id.radioButtonHigh)
            }

            binding.saveButton.text = "Update"
            mainActivity.supportActionBar?.title = "Update Item"

            if(itemEntity.title.contains("[")){
                val startIndex = itemEntity.title.indexOf("[") + 1
                val endIndex = itemEntity.title.indexOf("]")

                try {
                    val progress = itemEntity.title.substring(startIndex, endIndex).toInt()
                    binding.quantitySeekBar.progress = progress
                }catch (e: Exception){
                    //whoops
                }


            }
        }
    }

    override fun onPause() {
        super.onPause()
        sharedViewModel.transactionCompleteLiveData.postValue(false)
    }

    private fun saveItemEntityToDatabase() {
        val itemTitle = binding.titleEditText.text.toString().trim()
        if(itemTitle.isEmpty()){
            binding.titleTextField.error = "Required Field"
            return
        }

        binding.titleTextField.error = null

        val itemDescription = binding.descriptionEditTet.text.toString().trim()

        val itemPriority = when(binding.radioGroup.checkedRadioButtonId){
            R.id.radioButtonLow -> 1
            R.id.radioButtonMedium -> 2
            R.id.radioButtonHigh -> 3
            else -> 0
        }

        if(isInEditMode){
            val itemEntity = selectedItemEntity!!.copy(
                title = itemTitle,
                description = itemDescription,
                priority = itemPriority
            )

            sharedViewModel.updateItem(itemEntity)
            return
        }

        val itemEntity = ItemEntity(
            id = UUID.randomUUID().toString(),
            title = itemTitle,
            description = itemDescription,
            priority = itemPriority,
            createdAt = System.currentTimeMillis(),
            categoryId = ""
        )

       sharedViewModel.insertItem(itemEntity)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}