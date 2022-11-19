package com.gear.weathery.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter

class ViewPagerAdapter(val context: Context) : PagerAdapter() {
    var layoutInflater : LayoutInflater? = null

    val imgArray = arrayOf(
        R.drawable.onboard_pic1,
        R.drawable.onboard_pic2,
        R.drawable.onboard_pic3
    )

    val titleArray = arrayOf(
        "Before stepping outside your comfort zone, familiarize yourself with the weather",
        "Plan your whole day while weathery guides your movement",
        "No matter the weather, you are always informed and prepared"
    )

    override fun getCount(): Int {
        return titleArray.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.onboarding_pages_layout,container,false)
        val img = view.findViewById<ImageView>(R.id.onboarding_image)
        val titleTxt = view.findViewById<TextView>(R.id.title_text)
        // val desc = view.findViewById<TextView>(R.id.description)
        img.setImageResource(imgArray[position])
        titleTxt.setText(titleArray[position])
        // desc.setText(descriptionArray[position])
        container.addView(view)
        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)

    }

}