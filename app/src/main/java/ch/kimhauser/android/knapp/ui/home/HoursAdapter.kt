package ch.kimhauser.android.knapp.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import ch.kimhauser.android.knapp.R
import ch.kimhauser.android.knapp.data.HoursClass
import android.text.style.UnderlineSpan

import android.text.SpannableString

import android.net.Uri

import android.content.Intent
import androidx.core.view.isVisible
import java.util.*


class HoursAdapter(private val context: Context,
                   private val dataSource: List<HoursClass>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    //1
    override fun getCount(): Int {
        return dataSource.size
    }

    //2
    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //4
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val rowView = inflater.inflate(R.layout.list_item_kna_time, parent, false)
        val titleTextView = rowView.findViewById(R.id.recipe_list_title) as TextView
        val subtitleTextView = rowView.findViewById(R.id.recipe_list_subtitle) as TextView
        val lbl_closes_soon = rowView.findViewById(R.id.lbl_closes_soon) as TextView
        val detailTextView = rowView.findViewById(R.id.recipe_list_detail) as TextView

        val thumbnailImageView = rowView.findViewById(R.id.recipe_list_thumbnail) as ImageView
        val recipe = getItem(position) as HoursClass

        titleTextView.text = recipe.description /*recipe.place + " - " + */
        var addStart = ""
        if(recipe.start_minute < 10)
            addStart = "0"
        var startMinutes = addStart + recipe.start_minute.toString()
        var startTime = recipe.start_hour.toString() + ":" + startMinutes

        var addEnd = ""
        if(recipe.end_minute < 10)
            addEnd = "0"
        var endMinutes = addEnd + recipe.end_minute.toString()
        var endTime =  recipe.end_hour.toString() + ":" + endMinutes

        val tmpDate = Date()
        val calendarStart = Calendar.getInstance()
        calendarStart.time = tmpDate
        calendarStart.set(Calendar.HOUR_OF_DAY, recipe.start_hour)
        calendarStart.set(Calendar.MINUTE, recipe.start_minute)
        calendarStart.set(Calendar.SECOND, 0)

        val calendarEnd = Calendar.getInstance()
        calendarEnd.time = tmpDate
        calendarEnd.set(Calendar.HOUR_OF_DAY, recipe.end_hour)
        calendarEnd.set(Calendar.MINUTE, recipe.end_minute)
        calendarEnd.set(Calendar.SECOND, 0)
        val difTime = calendarEnd.time.time - calendarStart.time.time

        var closesSoon = false
        if((difTime / 1000 / 60) <= 60/* || (difTime / 1000 / 60) >= -60*/){
            println("CLOSES SOON!!!")
            closesSoon = true
        }
        //calendarEnd.time // Your changed date object

        subtitleTextView.text = startTime + " - " + endTime
        val spanStr = SpannableString(recipe.address.toString())
        spanStr.setSpan(UnderlineSpan(), 0, spanStr.length, 0)
        detailTextView.setText(spanStr)
        detailTextView.setOnClickListener {
            val geoIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "geo:0,0?q="
                            + detailTextView.text.toString(),
                ),
            )
            parent.context.startActivity(geoIntent)
        }
        lbl_closes_soon.setText("Schliesst bald!")
        lbl_closes_soon.isVisible = closesSoon
        return rowView
    }
}