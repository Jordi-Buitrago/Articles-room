package com.example.article

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.article.room.Article
import com.example.article.room.ArticleDao
import com.example.article.room.MyDb
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var db: MyDb
    private lateinit var dao: ArticleDao
    private lateinit var articles: List<Article>
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        setupDatabase()
    }

    private fun setupDatabase() {
        db = Room.databaseBuilder(
            applicationContext,
            MyDb::class.java, "Articles"
        ).build()

        dao = db.ArticleDao()
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        lifecycleScope.launch {
            dao.insert(Article("2", "wpasaloksod", "family", 1.0f, 1.0f))
            articles = dao.getArticles()
            recyclerView.adapter = ArticleAdapter(articles, dao, this@MainActivity)
        }

/*        lifecycleScope.launch {
            //articles = dao.getArticles()
            dao.insert(Article("1", "description", "family", 1.0f, 1.0f))
            articles = dao.getArticles()

        }.invokeOnCompletion {
            recyclerView.adapter = ArticleAdapter(articles, dao, this)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(this)
        }*/
    }

    /*private fun addItem() {
        lifecycleScope.launch {
            val article = Article("1", "description", "family", 1.0f, 1.0f)
            dao.insert(article)
        }
    }*/

}