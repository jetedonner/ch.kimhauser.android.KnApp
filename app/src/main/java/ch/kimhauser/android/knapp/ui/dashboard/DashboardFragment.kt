package ch.kimhauser.android.knapp.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ch.kimhauser.android.knapp.R
import ch.kimhauser.android.knapp.data.KnAClass
import ch.kimhauser.android.knapp.databinding.FragmentDashboardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.URL
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.text.method.ScrollingMovementMethod

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.txtLog.setMovementMethod(ScrollingMovementMethod())

        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        if (sharedPref != null) {

            val wsURL = sharedPref.getString(getString(R.string.preference_online_sync_url), getString(R.string.preference_online_sync_url_std))
            binding.txtUpdateURL.setText(wsURL)
            binding.swtAutoSync.isChecked = sharedPref.getBoolean(getString(R.string.preference_auto_online_sync), false)

            try {
                GlobalScope.launch(Dispatchers.Default) {  // replaces doAsync
                    try {
                        val apiResponse = URL(wsURL + "/knasng").readText()
                        //var teste = apiResponse
                        launch(Dispatchers.Main) { // replaces uiThread

                            appendLog(apiResponse)
                            System.out.println(apiResponse)

                            val typeToken = object : TypeToken<List<KnAClass>>() {}.type
                            val knas = Gson().fromJson<List<KnAClass>>(apiResponse, typeToken)

                            for (kna in knas) {
                                appendLog("========= KnA: =========")
                                appendLog("Place: " + kna.place)
                                appendLog("Desc: " + kna.description)
                                appendLog("Address: " + kna.address)
                                appendLog("========================")
                            }
                        }
                    }catch (e2:Exception){
                        var exep = e2.message
                        appendLog(exep.toString())
                    }
                }
            }catch (e:Exception){
                var exep = e.message
            }
        }
        return root
    }

    fun appendLog(msg:String){
        binding.txtLog.append("\n" + msg)
    }

    override fun onDestroyView() {
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        if (sharedPref != null) {
            sharedPref.edit()
                .putBoolean(getString(R.string.preference_auto_online_sync), binding.swtAutoSync.isChecked)
                .putString(getString(R.string.preference_online_sync_url), binding.txtUpdateURL.text.toString())
                .apply()
        }

        super.onDestroyView()
        _binding = null
    }
}