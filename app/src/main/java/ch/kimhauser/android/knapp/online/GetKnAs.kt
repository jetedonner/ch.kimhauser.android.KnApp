package ch.kimhauser.android.knapp.online

import ch.kimhauser.android.knapp.data.HoursClass
import ch.kimhauser.android.knapp.data.KnAClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.URL
import java.util.*

class GetKnAs {

    fun getAllKnAs(wsURL:String, result: (knas: List<KnAClass>) -> Unit) {
        try {
            GlobalScope.launch(Dispatchers.Default) {  // replaces doAsync
                try {
                    val apiResponse = URL(wsURL + "/knasng").readText()
                    launch(Dispatchers.Main) { // replaces uiThread
                        System.out.println(apiResponse)
                        val typeToken = object : TypeToken<List<KnAClass>>() {}.type
                        val knas = Gson().fromJson<List<KnAClass>>(apiResponse, typeToken)
                        result(knas)
                    }
                }catch (e2: Exception){
                    var exep = e2.message
                }
            }
        }catch (e: Exception){
            var exep = e.message
        }
    }

    fun getOpen(wsURL:String, result: (hours: List<HoursClass>) -> Unit) {
        try {
            GlobalScope.launch(Dispatchers.Default) {
                try {
                    val calendar: Calendar = Calendar.getInstance()
                    val day: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
                    val apiResponse = URL(wsURL + "/openng/" + day.toString()).readText()
                    launch(Dispatchers.Main) {
                        println(apiResponse)
                        val typeToken = object : TypeToken<List<HoursClass>>() {}.type
                        val hours = Gson().fromJson<List<HoursClass>>(apiResponse, typeToken)
                        result(hours)
                    }
                }catch (e2: Exception){
                    var exep = e2.message
                }
            }
        }catch (e: Exception){
            var exep = e.message
        }
    }
}