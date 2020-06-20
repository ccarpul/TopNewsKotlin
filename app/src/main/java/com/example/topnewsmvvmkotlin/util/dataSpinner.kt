package com.example.topnewsmvvmkotlin.util

import android.view.View
import android.widget.Spinner
import com.example.topnewsmvvmkotlin.R

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



