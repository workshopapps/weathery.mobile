package com.gear.weathery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gear.weathery.databinding.FragmentPersonalDataBinding
import com.gear.weathery.databinding.FragmentProfileBinding


class PersonalDataFragment : Fragment() {

    private var _binding: FragmentPersonalDataBinding? =  null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPersonalDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.updatePersonalInfoId?.setOnClickListener {

            findNavController().navigateUp()
        }
    }
}