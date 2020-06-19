package com.example.topnewsmvvmkotlin.ui.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.ui.filters.ModelSpinner

class FilterAdapterSpinner(context: Context?, private val listItemSpinner: ArrayList<ModelSpinner>) :
    ArrayAdapter<ModelSpinner>(context!!, 0, listItemSpinner) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        var mConvertView: View? = convertView
            mConvertView = LayoutInflater.from(context)
                .inflate(R.layout.spinner_style, parent, false)

        val textViewSpinner: TextView? = mConvertView?.findViewById(R.id.textviewValuesFilters)
        val modelSpinner: ModelSpinner? = listItemSpinner[position]

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
