package com.openclassrooms.realestatemanager.controllers.fragments


import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.controllers.activities.DetailsActivity
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.viewmodels.Injection
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import com.openclassrooms.realestatemanager.viewmodels.ViewModelFactory
import com.openclassrooms.realestatemanager.views.ElementAdapter
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : Fragment() {

    private lateinit var adapter: ElementAdapter

    private lateinit var realEstateViewModel: RealEstateViewModel


    companion object {
        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureRecyclerView()
        configureViewModel()
        getElements()
    }


    // Configure RecyclerView and assign the click handler to the Adapter
    private fun configureRecyclerView() {


        adapter = ElementAdapter { realEstate: RealEstate -> startDetailsActivityOnClick(realEstate) }
        main_recycler_view.adapter = adapter
        main_recycler_view.layoutManager = LinearLayoutManager(activity)
        main_recycler_view.addItemDecoration(DividerItemDecoration(
                main_recycler_view.context, DividerItemDecoration.VERTICAL))
    }


    // Pass data to intent and start DetailsActivity when clicked
    private fun startDetailsActivityOnClick(realEstate: RealEstate) {

        val intent = Intent(activity, DetailsActivity::class.java)
        intent.putExtra("REAL ESTATE", realEstate)
        startActivity(intent)
    }


    private fun updateList(list: List<RealEstate>) {

        this.adapter.updateData(list)
    }


    private fun configureViewModel() {

        val viewModelFactory = Injection.provideViewModelFactory(requireContext())
        realEstateViewModel = ViewModelProviders.of(this,
                viewModelFactory).get(RealEstateViewModel::class.java)
    }


    private fun getElements() {

        realEstateViewModel.getAllRealEstates().observe(this,
                Observer<List<RealEstate>> { updateList(it) })
    }


}



