package com.gear.weathery.dashboard.ui.viewPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gear.weathery.dashboard.R
import com.gear.weathery.dashboard.databinding.FragmentObjectCollectionBinding

class CollectionObjectFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentObjectCollectionBinding.inflate(inflater, container, false).root
    }

}
