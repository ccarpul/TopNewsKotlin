package com.example.topnewsmvvmkotlin.utils

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.ui.adapter.FilterAdapterSpinner
import com.example.topnewsmvvmkotlin.ui.filters.ModelSpinner
import kotlinx.android.synthetic.main.navigationdrawer_body.view.*

val imageSources = intArrayOf(
    R.drawable.language_icon, R.drawable.ic_abc_news, R.drawable.ic_bbc_news, R.drawable.ic_bbc_news,
    R.drawable.ic_bloomberg, R.drawable.ic_cnn, R.drawable.ic_cnn, R.drawable.ic_espn,
    R.drawable.ic_fox_news, R.drawable.ic_fox_news, R.drawable.ic_globo, R.drawable.ic_google2)
val flagLanguages = intArrayOf(
    R.drawable.language_icon, R.drawable.ic_us, R.drawable.ic_ve,
    R.drawable.ic_fr, R.drawable.ic_it, R.drawable.ic_pt)
val flagCountries = intArrayOf(
    R.drawable.language_icon, R.drawable.ic_ar, R.drawable.ic_br, R.drawable.ic_ca,
    R.drawable.ic_de, R.drawable.ic_fr, R.drawable.ic_gb, R.drawable.ic_it, R.drawable.ic_mx,
    R.drawable.ic_pt, R.drawable.ic_us, R.drawable.ic_ve)


fun View.builtSpinners(){
    spinnerFilterCountry .built(R.array.countries , flagCountries)
    spinnerFilterCategory.built(R.array.categories, flagCountries)
    spinnerFilterSource  .built(R.array.sources   , imageSources)
    spinnerFilterLanguage.built(R.array.languages , flagLanguages)
}

fun Spinner.built(arraySpinnerNames: Int, listImages: IntArray){

    val listFilterSpinner: ArrayList<ModelSpinner> = arrayListOf()
    val itemsSpinnerName:  Array<String>           = resources.getStringArray(arraySpinnerNames)
    for (item in itemsSpinnerName.indices) {
        listFilterSpinner.add( ModelSpinner(itemsSpinnerName[item], listImages[item]) )
    }
    this.adapter = FilterAdapterSpinner(context, listFilterSpinner)
}

fun Spinner.getValue(spinnerValues: Int):String{

    if(this.selectedItemPosition > 0)
        return context.resources.getStringArray(spinnerValues)[this.selectedItemPosition]
    return Constants.FIELD_EMPTY
}

fun Spinner.hide(){
    visibility = View.INVISIBLE
    setSelection(0)
}

fun View.hide(){ isVisible = false}  //Using KTX

fun View.show(){ isVisible = true }

fun RecyclerView.isLastArticleDisplayed(linearLayoutManager: LinearLayoutManager): Boolean {

    val totalItems = this.adapter?.itemCount
    if (totalItems != 0) {
        if (RecyclerView.NO_POSITION != linearLayoutManager.findLastCompletelyVisibleItemPosition() &&
            linearLayoutManager.findLastCompletelyVisibleItemPosition() == totalItems?.minus(1)
        )
            return true
    }
    return false
}

fun makeToast(context: Context?, message: String) {

    val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
    val layout = toast.view as LinearLayout
    layout.setBackgroundResource(R.drawable.shape_toast)
    layout.setPadding(20)
    val viewMessage = layout.getChildAt(0) as TextView
    viewMessage.setTextColor(Color.WHITE)
    viewMessage.textSize = 16f
    toast.apply {
        setGravity(Gravity.BOTTOM, 0, 200)
        show()
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}