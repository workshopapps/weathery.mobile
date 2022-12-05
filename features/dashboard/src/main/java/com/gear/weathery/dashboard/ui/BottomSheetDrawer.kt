package com.gear.weathery.dashboard.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.gear.weathery.dashboard.databinding.BottomSheetDrawerBinding
import com.gear.weathery.dashboard.util.OnClickEvent
import com.gear.weathery.location.api.Location
import com.gear.weathery.location.api.LocationsRepository
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BottomSheetDrawer : BottomSheetDialogFragment() {

    private var _binding: BottomSheetDrawerBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DashboardViewModel
    private val locationAdapter by lazy { SavedLocationAdapter() }
    private var savedLocationList = emptyList<Location>()
    private lateinit var onClickEvent: OnClickEvent

    @Inject
    lateinit var locationsRepository: LocationsRepository


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelProviderFactory =
            DashboardViewModel.DashboardViewModelFactory(locationsRepository)
        viewModel = ViewModelProvider(
            requireParentFragment(),
            viewModelProviderFactory
        )[DashboardViewModel::class.java]
        viewModel.getSavedLocation()
        onClickEvent = requireParentFragment() as OnClickEvent


        lifecycleScope.launch {
            viewModel.locationFlow.collect {
                savedLocationList = it
                locationAdapter.submitList(it)
            }
        }

        binding.savedLocationRecyclerView.adapter = locationAdapter
        locationAdapter.adapterClick {
            onClickEvent.onSavedLocationClicked(it.latitude, it.longitude)
            this.dismiss()
        }
        binding.searchView.isActivated = false
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val searchList = savedLocationList.filter {
                        it.name.lowercase().contains(query.lowercase()) || it.country.lowercase()
                            .contains(query.lowercase()) ||
                                it.name.uppercase()
                                    .contains(query.uppercase()) || it.country.uppercase()
                            .contains(query.uppercase())
                    }
                    locationAdapter.submitList(searchList)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val searchList = savedLocationList.filter {
                        it.name.lowercase().contains(newText.lowercase()) || it.country.lowercase()
                            .contains(newText.lowercase()) ||
                        it.name.uppercase()
                            .contains(newText.uppercase()) || it.country.uppercase()
                            .contains(newText.uppercase())
                    }
                    locationAdapter.submitList(searchList)
                }
                return true
            }
        })


    }

}