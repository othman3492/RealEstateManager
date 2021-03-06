package com.openclassrooms.realestatemanager.view.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.view.ui.fragments.DetailsFragment
import com.openclassrooms.realestatemanager.view.ui.fragments.ListFragment
import com.openclassrooms.realestatemanager.view.ui.fragments.MapFragment
import com.openclassrooms.realestatemanager.model.RealEstate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_toolbar.*


class MainActivity : AppCompatActivity() {


    private var isTablet = false
    private var realEstate = RealEstate()
    private var fragmentId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {

            fragmentId = savedInstanceState.getInt("FRAGMENT_ID")
        }

        setSupportActionBar(main_toolbar)
        updateUIWhenCreating(fragmentId)
        configureBottomNavigationView()


        // Verify if device is a tablet
        val detailsFragment = findViewById<View>(R.id.details_fragment_container)
        isTablet = detailsFragment?.visibility == View.VISIBLE

    }

    // Save current fragment ID to retrieve it after rotation
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("FRAGMENT_ID", fragmentId)
    }


    //----------------------------
    //USER INTERFACE
    //----------------------------


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_add -> startActivity(Intent(this, AddEditActivity::class.java))
            R.id.menu_loan -> startActivity(Intent(this, LoanActivity::class.java))
            R.id.menu_search -> startActivity(Intent(this, SearchActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }


    // Enable back button if fragment isn't home screen
    override fun onBackPressed() {

        val count = supportFragmentManager.backStackEntryCount

        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }


    private fun updateUIWhenCreating(fragmentId: Int) {

        if (intent.getStringExtra("QUERY") != null) {

            val query = intent.getStringExtra("QUERY")

            displayFragment(ListFragment.newInstance(query))
            displaySecondFragment(DetailsFragment.newInstance(realEstate))

            // Modify bottom navigation menu to add back button
            bottom_nav_view.visibility = View.GONE
            back_nav_view.visibility = View.VISIBLE

        } else {

            if (fragmentId == 0) {
                displayFragment(ListFragment.newInstance(null))
                displaySecondFragment(DetailsFragment.newInstance(realEstate))
            } else if (fragmentId == 1) {
                displayFragment(MapFragment.newInstance())
                displaySecondFragment(DetailsFragment.newInstance(realEstate))
            }
        }
    }


    private fun configureBottomNavigationView() {

        // Configure bottom buttons
        bottom_nav_view.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.bottom_list_view -> displayFragment(ListFragment.newInstance(null))
                R.id.bottom_map_view -> displayFragment(MapFragment.newInstance())
            }

            return@setOnNavigationItemSelectedListener true
        }

        // Configure bottom back button
        back_nav_view.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.back_bottom -> {
                    displayFragment(ListFragment.newInstance(null))
                    back_nav_view.visibility = View.GONE
                    bottom_nav_view.visibility = View.VISIBLE
                }
            }

            return@setOnNavigationItemSelectedListener true
        }
    }


    private fun displayFragment(fragment: Fragment) {

        // Set fragmentId to save it into SavedInstanceState
        when (fragment) {
            is ListFragment -> {
                fragmentId = 0
            }
            is MapFragment -> {
                fragmentId = 1
            }
            is DetailsFragment -> {
                fragmentId = 2
            }
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.fragment_container, fragment).commit()

    }


    // Display DetailsFragment is device is a tablet
    private fun displaySecondFragment(fragment: Fragment) {

        if (isTablet) {

            val secondTransaction = supportFragmentManager.beginTransaction()
            secondTransaction.addToBackStack(null)
            secondTransaction.replace(R.id.details_fragment_container, fragment).commit()
        }
    }


}
