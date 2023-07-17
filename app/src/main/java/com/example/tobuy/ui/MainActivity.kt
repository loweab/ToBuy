package com.example.tobuy.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.content.getSystemService
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController

import com.example.tobuy.R
import com.example.tobuy.arch.ToBuyViewModel
import com.example.tobuy.database.AppDatabase

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: ToBuyViewModel by viewModels()
        viewModel.init(AppDatabase.getDatabase(this))

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun hideKeyboard(view: View){
        val imm: InputMethodManager = application.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard(view: View){
        val imm: InputMethodManager = application.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //imm.showSoftInput(view, 0)
        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

}