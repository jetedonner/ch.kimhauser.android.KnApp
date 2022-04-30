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
import ch.kimhauser.android.knapp.data.KnAClass
import android.text.style.UnderlineSpan

import android.text.SpannableString
import androidx.core.content.ContextCompat.startActivity

import android.net.Uri

import android.content.Intent
import androidx.core.content.ContextCompat


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
        // Get view for row item
        val rowView = inflater.inflate(R.layout.list_item_recipe, parent, false)

        // Get title element
        val titleTextView = rowView.findViewById(R.id.recipe_list_title) as TextView

// Get subtitle element
        val subtitleTextView = rowView.findViewById(R.id.recipe_list_subtitle) as TextView

// Get detail element
        val detailTextView = rowView.findViewById(R.id.recipe_list_detail) as TextView

// Get thumbnail element
        val thumbnailImageView = rowView.findViewById(R.id.recipe_list_thumbnail) as ImageView

        // 1
        val recipe = getItem(position) as HoursClass

// 2
        titleTextView.text = recipe.place + " - " + recipe.description
        var addStart = ""
        if(recipe.start_minute < 10)
            addStart = "0"
        var addEnd = ""
        if(recipe.end_minute < 10)
            addEnd = "0"
        subtitleTextView.text = recipe.start_hour.toString() + ":" + addStart + recipe.start_minute.toString() + " - " + recipe.end_hour.toString() + ":" + addEnd + recipe.end_minute.toString()
        val spanStr = SpannableString(recipe.address.toString())
        spanStr.setSpan(UnderlineSpan(), 0, spanStr.length, 0)
        detailTextView.setText(spanStr)
        detailTextView.setOnClickListener {
            val geoIntent = Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    "geo:0,0?q="
                            + detailTextView.getText().toString()
                )
            )
            parent.context.startActivity(geoIntent)
//            startActivity(context, geoIntent)
        }
        //detailTextView.text = "Address: " + recipe.address

// 3
        //Picasso.with(context).load(recipe.imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView)

        return rowView
    }
}