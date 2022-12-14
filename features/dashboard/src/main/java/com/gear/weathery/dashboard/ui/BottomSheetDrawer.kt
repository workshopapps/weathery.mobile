package com.gear.weathery.dashboard.ui


import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.dashboard.R
import com.gear.weathery.dashboard.databinding.BottomSheetDrawerBinding
import com.gear.weathery.dashboard.util.OnClickEvent
import com.gear.weathery.location.api.Location
import com.gear.weathery.location.api.LocationsRepository
import com.gear.weathery.setting.notifications.database.NotificationDao
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BottomSheetDrawer : BottomSheetDialogFragment() {

    private var _binding: BottomSheetDrawerBinding? = null
    private val binding get() = _binding!!
    private val locationAdapter by lazy { SavedLocationAdapter() }
    private var savedLocationList = emptyList<Location>()
    private lateinit var onClickEvent: OnClickEvent

    @Inject
    lateinit var locationsRepository: LocationsRepository


    @Inject
    lateinit var notificationDao: NotificationDao
    private val viewModel: DashboardViewModel by activityViewModels {
        DashboardViewModel.DashboardViewModelFactory(
            locationsRepository,notificationDao, settingsPreference
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    @Inject
    lateinit var settingsPreference: SettingsPreference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickEvent = requireParentFragment() as OnClickEvent
        adaptiveSearchView()

        toggleEmptyState(savedLocationList)
        lifecycleScope.launch {
            viewModel.locationFlow.collect {locationList->
                savedLocationList = locationList
                locationAdapter.submitList(locationList)
                toggleEmptyState(locationList)
            }
        }
        binding.savedLocationRecyclerView.adapter = locationAdapter
        locationAdapter.adapterClick {location->
            onClickEvent.onSavedLocationClicked(location.latitude, location.longitude)
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
                    toggleEmptyState(searchList)
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
                    toggleEmptyState(searchList)
                }
                return true
            }
        })


    }
    private fun adaptiveSearchView(){
        val  searchEditText =
            binding.searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText

        val searchIcon =
            binding.searchView.findViewById(androidx.appcompat.R.id.search_mag_icon) as ImageView

        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                searchEditText.setTextColor(ContextCompat.getColor(requireContext(),android.R.color.white))
                searchEditText.setHintTextColor(ContextCompat.getColor(requireContext(),android.R.color.white))
                searchIcon.setImageResource(R.drawable.search_icon_dark_mode)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                searchEditText.setHintTextColor(
                    ContextCompat.getColor(requireContext(),
                        R.color.weathery_grey_4
                    )
                )
                searchIcon.setImageResource(R.drawable.search_icon_light_mode)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                searchEditText.setHintTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
            }
        }

    }

  private  fun toggleEmptyState(list:List<Location>){
      val isEmpty = locationAdapter.currentList.isEmpty() || list.isEmpty() && locationAdapter.itemCount==0
      binding.emptyView.isVisible = isEmpty
      binding.savedLocationRecyclerView.isVisible = !isEmpty
      binding.emptyStateText.isVisible = isEmpty
    }

}