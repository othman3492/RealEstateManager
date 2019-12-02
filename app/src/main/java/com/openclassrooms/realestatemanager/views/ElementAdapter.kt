package com.openclassrooms.realestatemanager.views

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.RealEstate
import kotlinx.android.synthetic.main.list_element_layout.view.*


// Create Adapter with a click listener (parameter RealEstate, return nothing)
class ElementAdapter(private val clickListener: (RealEstate) -> Unit) :
        RecyclerView.Adapter<ElementAdapter.ElementViewHolder>() {


    private var elements: List<RealEstate> = ArrayList()


    override fun getItemCount() = elements.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementViewHolder {

        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.list_element_layout, parent, false)
        return ElementViewHolder(v)
    }

    // Populate ViewHolder with data depending on the position in the list, and configure the click
    override fun onBindViewHolder(holder: ElementViewHolder, position: Int) {

        holder.bind(elements[position], clickListener)
    }


    fun updateData(list: List<RealEstate>) {

        this.elements = list
        this.notifyDataSetChanged()
    }







    class ElementViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {


        private var view: View = v
        private var types = arrayOf("House", "Appartment", "Building")


        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Log.d("TAG", "Click")
        }

        companion object {

            private val POSITION = "POSITION"
        }


        // Assign data to the views and handle click events through a function parameter
        fun bind(realEstate: RealEstate, clickListener: (RealEstate) -> Unit) {

            view.element_type.text = types[requireNotNull(realEstate.type)]
            view.element_location.text = realEstate.address?.city
            view.element_price.text = String.format(R.string.price_in_dollars.toString(), realEstate.price)

            view.setOnClickListener { clickListener(realEstate) }
        }
    }


}


