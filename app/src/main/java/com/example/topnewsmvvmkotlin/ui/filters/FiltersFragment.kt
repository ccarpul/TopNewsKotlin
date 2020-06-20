package com.example.topnewsmvvmkotlin.ui.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.util.*
import kotlinx.android.synthetic.main.navigationdrawer_body.*

class FiltersFragment : Fragment(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    private var valuesFilterSpinner = arrayListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinners(view, context)
        spinnerFilterSource.onItemSelectedListener = this
        buttonSetupFilters.setOnClickListener(this)
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        valuesFilterSpinner = getValuesSpinner(activity)
        spinnerFilterCountry.apply {
            settingSpinner(spinnerFilterCountry, spinnerFilterCountry, valuesFilterSpinner) }
        spinnerFilterCategory.apply {
            settingSpinner(spinnerFilterCategory, spinnerFilterCategory, valuesFilterSpinner) }
    }
    override fun onClick(v: View) {
        var valuesFilterSpinnerToHomeFragment = ""
        for( valueFilter in getValuesSpinner(activity))  { valuesFilterSpinnerToHomeFragment += "$valueFilter," }
        val passValuesFilters
                = FiltersFragmentDirections.actionFiltersFragmentToHomeFragment()
                .setDefaulValuesFilter(valuesFilterSpinnerToHomeFragment)
        if(valuesFilterSpinnerToHomeFragment != ",,,,")
            Navigation.findNavController(v).navigate(passValuesFilters)
        else Toast.makeText(context, resources.getString(R.string.wrongChoice), Toast.LENGTH_SHORT).show()
    }
}
