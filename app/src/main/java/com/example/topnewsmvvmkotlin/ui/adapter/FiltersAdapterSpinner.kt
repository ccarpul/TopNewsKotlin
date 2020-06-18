package com.example.topnewsmvvmkotlin.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.ui.filters.ModelSpinner

class FilterAdapterSpinner(context: Context?, listItemSpinner: ArrayList<ModelSpinner>) :
    ArrayAdapter<ModelSpinner>(context!!, 0, listItemSpinner) {

    val listItemSpinner: ArrayList<ModelSpinner> = listItemSpinner

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        var mConvertView: View? = convertView

        if (convertView == null) {
            mConvertView = LayoutInflater.from(context)
                .inflate(R.layout.spinner_style, parent, false)
        }

        val textViewSpinner: TextView? = mConvertView?.findViewById(R.id.textviewValuesFilters)
        val modelSpinner: ModelSpinner? = listItemSpinner.get(position)

        textViewSpinner?.apply {
            text = modelSpinner?.textSpinner
                setCompoundDrawablesWithIntrinsicBounds(
                    modelSpinner!!.imageSpinner,
                    0, 0, 0
                )
        }
        return mConvertView!!
    }
}
