package ch.kimhauser.android.knapp.ui.home

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

    val simpleDateFormat = SimpleDateFormat("hh:mm:ss")
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

        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                updateTime()
                mainHandler.postDelayed(this, 1000)
            }
        })
        return root
    }

    fun updateTime(){
        if(txtTime != null) {
            val calendar: Calendar = Calendar.getInstance()
            val dateTime = simpleDateFormat.format(calendar.time)
            txtTime!!.text = dateTime
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}