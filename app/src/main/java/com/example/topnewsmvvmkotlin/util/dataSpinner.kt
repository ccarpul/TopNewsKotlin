package com.example.topnewsmvvmkotlin.util

import android.content.Context
import android.view.View
import android.widget.Spinner
import androidx.fragment.app.FragmentActivity
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.ui.adapter.FilterAdapterSpinner
import com.example.topnewsmvvmkotlin.ui.filters.ModelSpinner

val spinnerIdValues = arrayOf(

    intArrayOf(R.id.spinnerFilterCountry, R.array.countries, R.array.countryValues, 0),             //Country
    intArrayOf(R.id.spinnerFilterCategory, R.array.categories, R.array.categoryValues,1),           //Category
    intArrayOf(R.id.spinnerFilterSource, R.array.sources, R.array.sourceValues, 2),                 //Source
    intArrayOf(R.id.spinnerFilterLanguage, R.array.languages, R.array.languageValues, 3)            //Languages
    //intArrayOf(R.id.spinnerFilterSortby, R.array.sortBy, R.array.sortByValues, 4) /inability
)
val imageSources = intArrayOf(
    R.drawable.language_icon,
    R.drawable.ic_abc_news,
    R.drawable.ic_bbc_news,
    R.drawable.ic_bbc_news,
    R.drawable.ic_bloomberg,
    R.drawable.ic_cnn,
    R.drawable.ic_cnn,
    R.drawable.ic_espn,
    R.drawable.ic_fox_news,
    R.drawable.ic_fox_news,
    R.drawable.ic_globo,
    R.drawable.ic_google
)
val flagLanguages = intArrayOf(
    R.drawable.language_icon,
    R.drawable.ic_us,
    R.drawable.ic_ve,
    R.drawable.ic_fr,
    R.drawable.ic_it,
    R.drawable.ic_pt
)
val flagCountries = intArrayOf(
    R.drawable.language_icon,
    R.drawable.ic_ar,
    R.drawable.ic_br,
    R.drawable.ic_ca,
    R.drawable.ic_de,
    R.drawable.ic_fr,
    R.drawable.ic_gb,
    R.drawable.ic_it,
    R.drawable.ic_mx,
    R.drawable.ic_pt,
    R.drawable.ic_us,
    R.drawable.ic_ve
)
val category = flagCountries //Todo
val sortBy = flagCountries  //Todo
val imagesResources = arrayListOf(
     flagCountries, category, imageSources, flagLanguages,
     sortBy
)

fun settingSpinner(v: View, spinner: Spinner, valuesFilterSpinner: ArrayList<String>){
    if(valuesFilterSpinner[2] == "")  v.visibility = View.VISIBLE
    else{
        v. visibility = View.INVISIBLE
        spinner.setSelection(0)
    }
}

fun initSpinners(v: View, context: Context?){
    for ((idSpinnerLayout, arraySpinnerNames, arraySpinnerValues, pos) in spinnerIdValues) {
        val listFilterSpinner: ArrayList<ModelSpinner> = arrayListOf()
        val spinnerFilter: Spinner? = v.findViewById(idSpinnerLayout)
        val itemsSpinnerName: Array<String> = v.resources.getStringArray(arraySpinnerNames)
        for (item in itemsSpinnerName.indices) {
            listFilterSpinner.add(
                ModelSpinner(itemsSpinnerName[item], imagesResources[pos][item])
            )
        }
        spinnerFilter?.adapter = FilterAdapterSpinner(context, listFilterSpinner)
    }
}

fun getValuesSpinner(activity: FragmentActivity?): ArrayList<String> {

    var allItemsFiltersSelected = arrayListOf<String>()
    for ((spinnerId, spinnerName, spinnerValues) in spinnerIdValues) {
        val currentSpinner = activity?.findViewById<Spinner>(spinnerId)
        if (currentSpinner != null)
            if (currentSpinner.selectedItemPosition > 0) {
                allItemsFiltersSelected
                    .add(activity.resources.getStringArray(spinnerValues)[currentSpinner.selectedItemPosition])
            } else allItemsFiltersSelected.add("")
    }
    return allItemsFiltersSelected
}



