package com.openclassrooms.realestatemanager.controllers.activities

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.controllers.fragments.PhotoFragment
import com.openclassrooms.realestatemanager.models.Address
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewmodels.Injection
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import com.openclassrooms.realestatemanager.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.activity_add_edit.*
import java.io.IOException


class AddEditActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    // VIEWMODEL
    private lateinit var realEstateViewModel: RealEstateViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    // SPINNERS
    private var types: Array<String>? = null
    private var typeSpinnerTextView: TextView? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        init()
    }


    private fun init() {

        configureSpinner()
        configureViewModel()
        configureUI()
        configureButtons()
    }


    // Show/hide views depending on Add/Edit layout
    private fun configureUI() {

        if (intent.getSerializableExtra("REAL ESTATE") != null) {

            val realEstate = intent.getSerializableExtra("REAL ESTATE") as RealEstate

            // Set views' visibility
            add_property.visibility = View.GONE
            add_button.visibility = View.GONE
            edit_property.visibility = View.VISIBLE
            edit_button.visibility = View.VISIBLE

            // Fill input texts
            description_text_input.setText(realEstate.description)
            street_text_input.setText(realEstate.address?.street)
            postal_code_text_input.setText(realEstate.address?.postalCode)
            city_text_input.setText(realEstate.address?.city)
            surface_text_input.setText(realEstate.surface)
            price_text_input.setText(realEstate.price)
            agent_text_input.setText(realEstate.agent)
            rooms_text_input.setText(realEstate.nbRooms)
            bedrooms_text_input.setText(realEstate.nbBedrooms)
            bathrooms_text_input.setText(realEstate.nbBathrooms)

            // Update other views
            if (realEstate.status == true) {
                status_switch.isChecked = true
            }

            type_spinner.setSelection(realEstate.type!!)

        }

    }


    private fun configureViewModel() {

        viewModelFactory = Injection.provideViewModelFactory(this)
        realEstateViewModel = ViewModelProviders.of(this, viewModelFactory).get(RealEstateViewModel::class.java)
    }


    // Create RealEstate object from user data input
    private fun createObjectInDatabase() {

        val realEstate = RealEstate()
        getDataFromInput(realEstate)

        // Save object
        realEstateViewModel.createRealEstate(realEstate)

        // Return to MainActivity
        startActivity(Intent(this, MainActivity::class.java))

    }

    // Create RealEstate object from user data input
    private fun updateObjectInDatabase() {

        val realEstate = RealEstate()
        getDataFromInput(realEstate)

        // Update object
        realEstateViewModel.updateRealEstate(realEstate)

        // Return to DetailsActivity
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("REAL ESTATE", realEstate)
        startActivity(intent)

    }


    // Apply data to RealEstate object
    private fun getDataFromInput(realEstate: RealEstate) {

        realEstate.address = Address()

        realEstate.description = description_text_input.text.toString()
        realEstate.type = type_spinner.selectedItemPosition
        realEstate.address?.street = street_text_input.text.toString()
        realEstate.address?.postalCode = postal_code_text_input.text.toString()
        realEstate.address?.city = city_text_input.text.toString()
        realEstate.surface = surface_text_input.text.toString()
        realEstate.price = price_text_input.text.toString()
        realEstate.nbRooms = rooms_text_input.text.toString()
        realEstate.nbBedrooms = bedrooms_text_input.text.toString()
        realEstate.nbBathrooms = bathrooms_text_input.text.toString()
        realEstate.status = status_switch.isChecked
        realEstate.creationDate = Utils.convertDate(Utils.getTodayDate().toString())
        realEstate.agent = agent_text_input.text.toString()
        realEstate.latitude = getLocationFromAddress(baseContext, realEstate.address.toString())!!.latitude
        realEstate.longitude = getLocationFromAddress(baseContext, realEstate.address.toString())!!.longitude
    }


    // Get latitude/longitude from address string
    private fun getLocationFromAddress(context: Context, strAddress: String): LatLng? {

        val coder = Geocoder(context)
        val address: List<android.location.Address>?
        var latlng: LatLng? = null

        try {
            address = coder.getFromLocationName(strAddress, 3)
            if (address == null) {
                return null
            }
            val location = address[0]
            latlng = LatLng(location.latitude, location.longitude)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return latlng
    }


    // SPINNER CONFIGURATION
    private fun configureSpinner() {

        types = this.resources.getStringArray(R.array.types_array)

        type_spinner.onItemSelectedListener
        val typeArrayAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, types as Array<out String>)
        type_spinner.adapter = typeArrayAdapter
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        typeSpinnerTextView!!.text = types!![position]
    }


    // Configure add/edit and photo buttons
    private fun configureButtons() {

        manage_photos_button.setOnClickListener { displayDialogFragment() }
        add_button.setOnClickListener { createObjectInDatabase() }
        edit_button.setOnClickListener { updateObjectInDatabase() }
    }


    private fun displayDialogFragment() {

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            fragmentTransaction.remove(prev)
        }
        fragmentTransaction.addToBackStack(null)
        val dialogFragment = PhotoFragment()
        dialogFragment.show(fragmentTransaction, "dialog")
    }


}