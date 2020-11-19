package com.example.topnewsmvvmkotlin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.data.model.Article
import com.example.topnewsmvvmkotlin.data.model.ModelResponse
import com.google.android.material.shape.CornerFamily
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_splash.view.*
import kotlinx.android.synthetic.main.recycler_style.view.*


class ArticlesAdapterRecyclerView(
    private var list: MutableList<Article>,
    private val listener: OnClickSelectedItem

) : RecyclerView.Adapter<ArticlesAdapterRecyclerView.ArticlesAdapterViewHolder>() {

    private var pos = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesAdapterViewHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_style, parent, false)

        view.urlToImage.shapeAppearanceModel = view.urlToImage.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 50F)
            .setBottomRightCorner(CornerFamily.ROUNDED, 50F)
            .build()

        return ArticlesAdapterViewHolder(view)
    }

    fun addData(data: ModelResponse) {
        data.articles?.let { list.addAll(it) }
        notifyDataSetChanged()
    }

    fun getPosition(): Int = pos


    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ArticlesAdapterViewHolder, position: Int) {
        pos = position
        holder.bind(list[position])

    }

    interface OnClickSelectedItem {
        fun onClick(query: String?)
    }


    inner class ArticlesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private var url: String? = null


        fun bind(article: Article) {

            itemView.apply {
                articleId.text = article.source?.articleId
                name.text = article.source?.name
                title.text = article.title
                description.text = article.description
                content.text = article.content
                author.text = article.author
                publishedAt.text = article.publishedAt
                url = article.url

                if (!article.urlToImage.isNullOrBlank()) {
                    Picasso.with(itemView.context)
                        .load(article.urlToImage)
                        .placeholder(R.drawable.diarynews_image)
                        //.centerCrop()
                        .resize(360, 280)
                        .centerInside()
                        .into(urlToImage)
                } else itemView.urlToImage.setImageResource(R.drawable.diarynews_image)

                save.setOnClickListener {
                    save.setImageResource(R.drawable.ic_save)
                    listener.onClick("save")
                }
            }
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onClick(url)
        }
    }
}
