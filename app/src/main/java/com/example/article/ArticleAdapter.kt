package com.example.article

import android.content.ClipData.newIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.article.room.Article
import com.example.article.room.ArticleDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.widget.ArrayAdapter




class ArticleAdapter(
    var articles: MutableList<Article>,
    var dao: ArticleDao,
    var context: Context,
    var mainActivity: MainActivity
) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article, dao, context)

        holder.cardView.setOnClickListener {
            mainActivity.editArticle(article)
        }

        holder.delete.setOnClickListener {
            val dialog = android.app.AlertDialog.Builder(context)
            dialog.setTitle("Delete")
            dialog.setMessage("Are you sure you want to delete this article?")

            dialog.setPositiveButton("Yes") { dialog, which ->
                GlobalScope.launch {
                    dao.delete(article)
                }
                articles.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, articles.size)
            }
            dialog.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }


    // Holds the views for adding it to image and text
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val code = view.findViewById<TextView>(R.id.tvCode)
        val family = view.findViewById<TextView>(R.id.tvFamily)
        val price = view.findViewById<TextView>(R.id.tvPrice)
        val priceIVA = view.findViewById<TextView>(R.id.tvPriceWithVAT)
        val stock = view.findViewById<TextView>(R.id.tvStock)
        val description = view.findViewById<TextView>(R.id.tvDescription)

        val delete = view.findViewById<ImageView>(R.id.deleteButton)

        val cardView = itemView.findViewById<View>(R.id.card)

        fun bind(article: Article , dao: ArticleDao, context: Context) {
            code.text = article.codeArticle
            family.text = article.family
            price.text = article.pricenoIVA.toString()
            priceIVA.text = article.priceIVA.toString()
            description.text = article.description

            val stockValue = article.stock
            stock.text = stockValue.toString()

            if(stockValue <= 0) {
                stock.setTextColor(Color.parseColor("#FF0000"))
            }

            when(article.family){
                "software", "Software", "SOFTWARE" -> {
                    cardView.setBackgroundColor(Color.parseColor("#FFCDD2"))
                }
                "hardware", "Hardware", "HARDWARE" -> {
                    cardView.setBackgroundColor(Color.parseColor("#B2DFDB"))
                }
                "altres", "Altres", "ALTRES" -> {
                    cardView.setBackgroundColor(Color.parseColor("#BBDEFB"))
                }
            }
        }

    }
}

