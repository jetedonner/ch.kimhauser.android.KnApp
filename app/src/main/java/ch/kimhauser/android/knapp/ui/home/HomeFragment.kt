package ch.kimhauser.android.knapp.ui.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ch.kimhauser.android.knapp.R
import ch.kimhauser.android.knapp.data.HoursClass
import java.text.SimpleDateFormat
import java.util.*
import ch.kimhauser.android.knapp.data.KnAClass
import ch.kimhauser.android.knapp.databinding.FragmentHomeBinding
import ch.kimhauser.android.knapp.online.GetKnAs
import ch.kimhauser.android.knapp.ui.dashboard.DashboardViewModel

class HomeFragment : Fragment() {

    private val viewModel: DashboardViewModel by activityViewModels()

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private lateinit var listView: ListView

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

        viewModel.knas.observe(viewLifecycleOwner, Observer<List<KnAClass>> { knas ->
            // Update the selected filters UI
            //System.out.println(knas[0].place)
//            for (kna in knas){
//                binding.txtLogHome.append("\n" + kna.place)
//            }

//            listView = binding.recipeListView
//            val listItems = arrayOfNulls<String>(knas.size)
//// 3
//            for (i in 0 until knas.size) {
//                val recipe = knas[i]
//                listItems[i] = recipe.place
//            }
//// 4
//            val adapter =
//                context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, listItems) }
//            listView.adapter = adapter

//            val adapter = context?.let { HoursAdapter(it, knas) }
//            listView.adapter = adapter
        })


//        dashViewModel.knas.observe(this, Observer { item ->
//            // Perform an action with the latest item data
//        })

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.prgLoading.visibility == View.VISIBLE
        binding.prgLoading.isVisible = true
        txtTime = binding.txtTime
        listView = binding.recipeListView

//        binding.cmdLocate.setOnClickListener {
//            // Create a Uri from an intent string. Use the result to create an Intent.
//            val gmmIntentUri = Uri.parse("google.streetview:cbll=46.414382,10.013988")
//            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
//            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//            // Make the Intent explicit by setting the Google Maps package
//            mapIntent.setPackage("com.google.android.apps.maps")
//            // Attempt to start an activity that can handle the Intent
//            startActivity(mapIntent)
//        }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        if (sharedPref != null) {

            val wsURL = sharedPref.getString(
                getString(R.string.preference_online_sync_url), getString(
                    R.string.preference_online_sync_url_std
                )
            )
            if (wsURL != null) {
                GetKnAs().getAllKnAs(wsURL, fun(knas: List<KnAClass>) {
                    GetKnAs().getOpen(wsURL, fun(hours: List<HoursClass>) {
                        if(hours.size == 0){
                            binding.lblNoKnA.visibility == View.VISIBLE
                        }else {
                            binding.lblNoKnA.visibility == View.GONE
                            binding.lblNoKnA.isVisible = false
                            val adapter = context?.let { HoursAdapter(it, hours) }
                            listView.adapter = adapter
                        }
                        binding.prgLoading.visibility == View.GONE
                        binding.prgLoading.isVisible = false
                    })
                })
            }
        }
    }

    fun updateTime(){
        if(txtTime != null) {
            val calendar: Calendar = Calendar.getInstance()
            val dateTime = simpleDateFormat.format(calendar.time)
            homeViewModel._time.setValue(dateTime)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}