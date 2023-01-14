package com.gear.weathery.dashboard.ui

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.gear.weathery.common.preference.SettingsPreference
import com.gear.weathery.dashboard.R
import com.gear.weathery.dashboard.databinding.FragmentFeedbackBinding
import com.gear.weathery.dashboard.feedback.GMailSender
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FeedbackFragment : DialogFragment() {

    @Inject
    lateinit var settingsPreference: SettingsPreference

    private val viewModel: FeedbackViewModel by activityViewModels { FeedbackViewModel.FeedbackViewModelFactory() }

    private lateinit var binding: FragmentFeedbackBinding

    private lateinit var star1: ImageView
    private lateinit var star2: ImageView
    private lateinit var star3: ImageView
    private lateinit var star4: ImageView
    private lateinit var star5: ImageView
    private lateinit var starsList: List<ImageView>
    private lateinit var thankYouGroup: LinearLayout
    private lateinit var feedbackGroup: ConstraintLayout
    private lateinit var submitTextView: TextView
    private lateinit var submittingTextView: TextView

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = FragmentFeedbackBinding.inflate(inflater, container, false)
//
//        binding.apply {
//            lifecycleOwner = viewLifecycleOwner
//            feedbackFragment = this@FeedbackFragment
//            viewmodel = viewModel
//        }
//
//        return binding.root
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;
            binding = FragmentFeedbackBinding.inflate(inflater, null, false)

            binding.apply {
                lifecycleOwner = this@FeedbackFragment
                feedbackFragment = this@FeedbackFragment
                viewmodel = viewModel
            }

            completeSetup()


            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(binding.root)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        completeSetup()
//
//    }

    private fun completeSetup() {
        binding.apply {
            star1 = star1ImageView
            star2 = star2ImageView
            star3 = star3ImageView
            star4 = star4ImageView
            star5 = star5ImageView
            thankYouGroup = thankYouGroupLinearLayout
            feedbackGroup = feedbackGroupConstraintLayout
            submitTextView = submitButtonTextView
            submittingTextView = submittingButtonTextView
        }
        starsList = listOf(star1, star2, star3, star4, star5)

        binding.clickSurfaceView.setOnClickListener {
            binding.messageEditText.requestFocus()
            val inputMethodManager =
                requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(
                binding.messageEditText,
                InputMethodManager.SHOW_IMPLICIT
            )
        }

        viewModel.state.observe(this) {
            when (it) {
                PASSED -> {
                    thankYouGroup.visibility = View.VISIBLE
                    feedbackGroup.visibility = View.INVISIBLE
                    submitTextView.visibility = View.GONE
                    submittingTextView.visibility = View.GONE
                }

                BUSY -> {
                    thankYouGroup.visibility = View.INVISIBLE
                    feedbackGroup.visibility = View.VISIBLE
                    submitTextView.visibility = View.GONE
                    submittingTextView.visibility = View.VISIBLE
                }

                else -> {
                    thankYouGroup.visibility = View.INVISIBLE
                    feedbackGroup.visibility = View.VISIBLE
                    submitTextView.visibility = View.VISIBLE
                    submittingTextView.visibility = View.GONE
                }
            }
        }

        binding.messageEditText.addTextChangedListener {
            viewModel.updateCharCount(it.toString().length)
        }


    }

    fun selectRating(rating: Int) {
        for (star in starsList) {
            star.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.weathery_grey_6
                ), android.graphics.PorterDuff.Mode.SRC_IN
            )
        }

        for (index in 0 until rating) {
            starsList[index].setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.weathery_orange
                ), android.graphics.PorterDuff.Mode.SRC_IN
            )
        }

        viewModel.rating = rating
    }

    fun sendFeedback() {
        val message = binding.messageEditText.text.toString()
        if (message.isBlank()) {
            viewModel.sendReview(requireContext())
        } else {
            viewModel.sendReview(requireContext(), message)
        }
    }

    fun goToHome() {
        viewModel.reset()
        dismissAllowingStateLoss()
    }
}