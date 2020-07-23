package com.example.topnewsmvvmkotlin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.topnewsmvvmkotlin.R
import com.example.topnewsmvvmkotlin.data.model.Article
import com.example.topnewsmvvmkotlin.data.model.ModelResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_style.view.*

class ArticlesAdapterRecyclerView(
    private var list: MutableList<Article>,
    private val listener: OnClickSelectedItem

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val originalList: MutableList<Article> = arrayListOf()

    private var pos =0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_style, parent, false)
        return ArticlesAdapterViewHolder(view)
    }

    fun addData(data: ModelResponse) {
        list.addAll(data.articles)
        originalList.addAll(list)
        notifyDataSetChanged()
    }

    fun getPosition(): Int{
        return pos
    }

    fun getOriginalList(): MutableList<Article> {
        return originalList
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ArticlesAdapterViewHolder) {
            pos = position
            holder.bind(list[position])
        }
    }

    interface OnClickSelectedItem { fun onClick(query: String) }


    inner class ArticlesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var url: String = ""
        fun bind(article: Article) {
            itemView.apply {
                article_id.text = article.source.id
                name.text = article.source.id
                title.text = article.title
                description.text = article.description
                content.text = article.content
                author.text = article.author
                publishedAt.text = article.publishedAt
                url = article.url;
                if (!article.urlToImage.isNullOrBlank()) {
                    Picasso.with(itemView.context).load(article.urlToImage)
                        .placeholder(R.drawable.diarynews_image)
                        .resize(360, 280)
                        .centerCrop()
                        .into(urlToImage)
                }else urlToImage.setImageDrawable(resources.getDrawable(R.drawable.diarynews_image))
            }
            itemView.setOnClickListener(this)

        }
        override fun onClick(p0: View?) {listener.onClick(url)}
    }
}
