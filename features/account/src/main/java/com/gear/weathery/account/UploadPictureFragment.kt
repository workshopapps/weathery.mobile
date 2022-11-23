package com.gear.weathery.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.gear.weathery.databinding.FragmentPersonalDataBinding
import com.gear.weathery.databinding.FragmentProfileBinding
import com.gear.weathery.databinding.FragmentUploadPictureBinding


class UploadPictureFragment : Fragment() {

    private var _binding: FragmentUploadPictureBinding? =  null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUploadPictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.uploadPictureBtnId?.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}