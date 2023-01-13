package com.example.article

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.article.room.Article
import com.example.article.room.ArticleDao

class ArticleAdapter(
    private val articles: List<Article>,
    private val dao: ArticleDao,
    private val context: Context ) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val code = view.findViewById<TextView>(R.id.tvCode)
        val family = view.findViewById<TextView>(R.id.tvFamily)
        val price = view.findViewById<TextView>(R.id.tvPrice)
        val stock = view.findViewById<TextView>(R.id.tvStock)
        val description = view.findViewById<TextView>(R.id.tvDescription)

        fun bind(article: Article) {
            code.text = article.codeArticle
            family.text = article.family
            price.text = article.pricenoIVA.toString()
            stock.text = article.stock.toString()
            description.text = article.description
        }



    }
}
