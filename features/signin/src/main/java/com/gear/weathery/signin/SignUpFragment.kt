package com.gear.weathery.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.gear.weathery.signin.databinding.FragmentSignInBinding
import com.gear.weathery.signin.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var cancelSignUp: ImageView
    private lateinit var signIn: TextView

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancelSignUp = binding.cancelSignUp
        cancelSignUp.setOnClickListener{
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
        }

        signIn = binding.signIn
        signIn.setOnClickListener{
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }
}