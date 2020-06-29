package com.example.topnewsmvvmkotlin.util

import android.view.View
import android.widget.Spinner
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.ui.adapter.FilterAdapterSpinner
import com.example.topnewsmvvmkotlin.ui.filters.ModelSpinner
import kotlinx.android.synthetic.main.navigationdrawer_body.view.*

val imageSources = intArrayOf(
    R.drawable.language_icon, R.drawable.ic_abc_news, R.drawable.ic_bbc_news, R.drawable.ic_bbc_news,
    R.drawable.ic_bloomberg, R.drawable.ic_cnn, R.drawable.ic_cnn, R.drawable.ic_espn,
    R.drawable.ic_fox_news, R.drawable.ic_fox_news, R.drawable.ic_globo, R.drawable.ic_google)
val flagLanguages = intArrayOf(
    R.drawable.language_icon, R.drawable.ic_us, R.drawable.ic_ve,
    R.drawable.ic_fr, R.drawable.ic_it, R.drawable.ic_pt)
val flagCountries = intArrayOf(
    R.drawable.language_icon, R.drawable.ic_ar, R.drawable.ic_br, R.drawable.ic_ca,
    R.drawable.ic_de, R.drawable.ic_fr, R.drawable.ic_gb, R.drawable.ic_it, R.drawable.ic_mx,
    R.drawable.ic_pt, R.drawable.ic_us, R.drawable.ic_ve)

fun Spinner.built(arraySpinnerNames: Int, listImages: IntArray){

    val listFilterSpinner: ArrayList<ModelSpinner> = arrayListOf()
    val itemsSpinnerName:  Array<String>           = resources.getStringArray(arraySpinnerNames)
    for (item in itemsSpinnerName.indices) {
        listFilterSpinner.add( ModelSpinner(itemsSpinnerName[item], listImages[item]) )
    }
    this.adapter = FilterAdapterSpinner(context, listFilterSpinner)
}

fun View.builtSpinners(){
        spinnerFilterCountry .built(R.array.countries , flagCountries)
        spinnerFilterCategory.built(R.array.categories, flagCountries)
        spinnerFilterSource  .built(R.array.sources   , imageSources)
        spinnerFilterLanguage.built(R.array.languages , flagLanguages)
}

fun Spinner.getValue(spinnerValues: Int):String{

    if(this.selectedItemPosition > 0)
        return context.resources.getStringArray(spinnerValues)[this.selectedItemPosition]
    return Constants.FIELD_EMPTY
}

fun Spinner.hide(){
    this.apply{ visibility = View.GONE
                setSelection(0)
    }
}

fun Spinner.show(){ this.visibility = View.VISIBLE }

