package com.gear.weathery.dashboard.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.gear.weathery.dashboard.feedback.GMailSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedbackViewModel: ViewModel() {

    private var _state = MutableLiveData<Int>()
    val state: LiveData<Int> get() = _state

    private var _charCount = MutableLiveData<String>()
    val charCount: LiveData<String> get() = _charCount

    var rating = -1

    fun updateCharCount(count: Int){
        _charCount.value = "$count/900"
    }

    fun sendReview(context: Context, message: String = ""){
        viewModelScope.launch {
            if (rating == -1){
                    Toast.makeText(context, "No rating selected", Toast.LENGTH_LONG).show()
                return@launch
            }

            if(message.length > 900){
                    Toast.makeText(context, "Message too long. Must not exceed 900 characters.", Toast.LENGTH_LONG).show()
                return@launch
            }

            _state.value = BUSY

            try {
                viewModelScope.launch(Dispatchers.IO){
                    val feedback = "Rating: $rating/5 \n\n Message: $message"
                    val sender = GMailSender("tropicalweather.app@gmail.com", "pbijfbfwvgwjrinl")
                    sender.sendMail(
                        "Tropical Weather Android App review",
                        feedback,
                        "user",
                        "tropicalweather.app@gmail.com,support@tropicalweather.app"
                    )
                }.join()

                    _state.value = PASSED
            } catch (e: Exception) {
                Log.e("SendMail", e.message, e)
            }
        }
    }

    fun reset() {
        _state.value = DEFAULT
        _charCount.value = "0/900"
        rating = -1
    }

    init {
        _state.value = DEFAULT
        _charCount.value = "0/900"
    }

    class FeedbackViewModelFactory(): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FeedbackViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FeedbackViewModel() as T
            }
            throw IllegalArgumentException("Unknwon ViewModel class")
        }
    }
}