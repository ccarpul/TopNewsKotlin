package com.example.topnewsmvvmkotlin.ui.filters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.ui.MainActivity
import com.example.topnewsmvvmkotlin.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigationdrawer_body.*

class FiltersFragment : Fragment(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.builtSpinners()
        spinnerFilterSource.onItemSelectedListener = this
        buttonSetupFilters.setOnClickListener(this)

        (activity as MainActivity).toolBar.title = "Filters"

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        if (position > 0) {
            spinnerFilterCountry.hide()
            spinnerFilterCategory.hide()
        }else{
            spinnerFilterCountry.show()
            spinnerFilterCategory.show() }
    }

    override fun onClick(v: View) {

        val valuesFiltersToHomeFragment =
                "${spinnerFilterCountry.getValue(R.array.countryValues)}," +
                "${spinnerFilterCategory.getValue(R.array.categoryValues)}," +
                "${spinnerFilterSource.getValue(R.array.sourceValues)}," +
                "${spinnerFilterLanguage.getValue(R.array.languageValues)}," +
                "${editKeyWord.text} "

        if(valuesFiltersToHomeFragment != Constants.NO_FILTERS_SELECTED) {

            val passValuesFilters
                    = FiltersFragmentDirections
                     .actionFiltersFragmentToHomeFragment()
                     .setDefaulValuesFilter(valuesFiltersToHomeFragment)

            Navigation.findNavController(v).navigate(passValuesFilters)
        }
        else makeToast(context, resources.getString(R.string.wrongChoice))

    }
}
