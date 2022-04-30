package ch.kimhauser.android.knapp.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.*
import ch.kimhauser.android.knapp.R
import ch.kimhauser.android.knapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
    private var txtTime: TextView? = null //= binding.txtTime

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        txtTime = binding.txtTime

        binding.cmdLocate.setOnClickListener {
            // Create a Uri from an intent string. Use the result to create an Intent.
            val gmmIntentUri = Uri.parse("google.streetview:cbll=46.414382,10.013988")
            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps")
            // Attempt to start an activity that can handle the Intent
            startActivity(mapIntent)
        }

        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                updateTime()
                mainHandler.postDelayed(this, 1000)
            }
        })

        // Create the observer which updates the UI.
        val nameObserver = Observer<String> { newTime ->
            // Update the UI, in this case, a TextView.
            txtTime!!.text = newTime
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        homeViewModel._time.observe(this, nameObserver)

        return root
    }

    fun updateTime(){
        if(txtTime != null) {
            val calendar: Calendar = Calendar.getInstance()
            val dateTime = simpleDateFormat.format(calendar.time)
            //txtTime!!.text = dateTime
            homeViewModel._time.setValue(dateTime)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}