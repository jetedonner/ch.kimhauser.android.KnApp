package ch.kimhauser.android.knapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.kimhauser.android.knapp.data.KnAClass

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val mutableKnAs = MutableLiveData<List<KnAClass>>()
    val knas: LiveData<List<KnAClass>>  get() = mutableKnAs//.value.toMutableList()

    fun setKnAs(newKnas: List<KnAClass>) {
        mutableKnAs.value = newKnas
    }

//    fun selectItem(item: List<KnAClass>) {
//        mutableSelectedItem.value = item
//    }
//    private val _knas = MutableLiveData<List<KnAClass>>().apply {
//        value =
//    }
//    val knas: LiveData<List<KnAClass>> = _knas
}