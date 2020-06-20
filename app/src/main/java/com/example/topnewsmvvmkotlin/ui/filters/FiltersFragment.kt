package com.example.topnewsmvvmkotlin.ui.filters

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.ui.adapter.FilterAdapterSpinner
import com.example.topnewsmvvmkotlin.util.imagesResources
import com.example.topnewsmvvmkotlin.util.settingSpinner
import com.example.topnewsmvvmkotlin.util.spinnerIdValues
import kotlinx.android.synthetic.main.navigationdrawer_body.*

class FiltersFragment : Fragment(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    private var valuesFilterSpinner = arrayListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSpinner()
        spinnerFilterSource.onItemSelectedListener = this
        buttonSetupFilters.setOnClickListener(this)
        //getValuesSpinner(activity)
    }

    private fun initSpinner() {

        for ((idSpinnerLayout, arraySpinnerNames, arraySpinnerValues, pos) in spinnerIdValues) {

            val listFilterSpinner: ArrayList<ModelSpinner> = arrayListOf()
            val spinnerFilter: Spinner? = view?.findViewById(idSpinnerLayout)
            val itemsSpinnerName = resources.getStringArray(arraySpinnerNames)
            for (item in itemsSpinnerName.indices) {
                listFilterSpinner.add(
                    ModelSpinner(itemsSpinnerName[item], imagesResources[pos][item])
                )
            }
            spinnerFilter?.adapter = FilterAdapterSpinner(context, listFilterSpinner)
        }
    }

    private fun getValuesSpinner(activity: FragmentActivity?): ArrayList<String> {

        var allItemsFiltersSelected = arrayListOf<String>()
        for ((spinnerId, spinnerName, spinnerValues) in spinnerIdValues) {
            val currentSpinner = activity?.findViewById<Spinner>(spinnerId)
            if (currentSpinner != null)
                if (currentSpinner.selectedItemPosition > 0) {
                    allItemsFiltersSelected
                        .add(resources.getStringArray(spinnerValues)[currentSpinner.selectedItemPosition])
                } else allItemsFiltersSelected.add("")
        }
        return allItemsFiltersSelected
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        valuesFilterSpinner = getValuesSpinner(activity)

        spinnerFilterCountry.apply {
            settingSpinner(spinnerFilterCountry, spinnerFilterCountry, valuesFilterSpinner)
        }
        spinnerFilterCategory.apply {
            settingSpinner(spinnerFilterCategory, spinnerFilterCategory, valuesFilterSpinner)
        }
    }


    override fun onClick(v: View) {
        var valuesFilterSpinnerToHomeFragment = ""
        for( valueFilter in getValuesSpinner(activity))  { valuesFilterSpinnerToHomeFragment += "$valueFilter," }
        val passValuesFilters
                = FiltersFragmentDirections.actionFiltersFragmentToHomeFragment()
                .setDefaulValuesFilter(valuesFilterSpinnerToHomeFragment)
        if(valuesFilterSpinnerToHomeFragment != ",,,,")
            Navigation.findNavController(v).navigate(passValuesFilters)
        else Toast.makeText(context, "${resources.getString(R.string.wrongChoice)}", Toast.LENGTH_SHORT).show()
    }
}
