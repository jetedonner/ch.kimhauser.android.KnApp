package ch.kimhauser.android.knapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {

    val simpleDateFormat = SimpleDateFormat("HH:mm:ss")

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val _time = MutableLiveData<String>().apply {
        val calendar: Calendar = Calendar.getInstance()
        val dateTime = simpleDateFormat.format(calendar.time)
        value = dateTime
    }
    val time: LiveData<String> = _time
}