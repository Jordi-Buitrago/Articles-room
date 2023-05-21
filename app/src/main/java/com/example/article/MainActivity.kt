package com.example.article

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.article.room.Article
import com.example.article.room.ArticleDao
import com.example.article.room.MyDb
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var db: MyDb
    private lateinit var dao: ArticleDao
    private lateinit var articles: MutableList<Article>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArticleAdapter

    private val IVA_PERCENTAGE = 21.0



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
        ).fallbackToDestructiveMigration().build()

        dao = db.ArticleDao()
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        lifecycleScope.launch {
            //dao.insert(Article("2", "wpasaloksod", "family", 1.0f, 1.0f))
            articles = dao.getArticles()
            recyclerView.adapter = ArticleAdapter(articles, dao, this@MainActivity, this@MainActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                addArticle()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("MissingInflatedId")
    private fun addArticle() {
        val view = layoutInflater.inflate(R.layout.articles_dialog, null)

        // Obtiene una referencia al Spinner
        val spinner: Spinner = view.findViewById(R.id.spnFamily)
        val options = arrayOf(" ", "software", "hardware", "Altres")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)
        spinner.adapter = adapter

        val dialog = AlertDialog.Builder(this)
        dialog.setView(view)
        dialog.setTitle("Add Article")

        val rootView = window.decorView.findViewById<View>(android.R.id.content) // Obtener la vista principal de la actividad

        dialog.setPositiveButton("Add") { dialog, which ->
            val code = view.findViewById<EditText>(R.id.edtCode).text.toString()
            val description = view.findViewById<EditText>(R.id.edtDescription).text.toString()

            val spinner = view.findViewById<Spinner>(R.id.spnFamily)
            val family = spinner.selectedItem.toString()

            val price = view.findViewById<EditText>(R.id.edtPrice).text.toString().toFloat()
            // Calcula el precio con IVA
            val priceWithIVA = price * (1 + (IVA_PERCENTAGE / 100)).toFloat()

            val stock = view.findViewById<EditText>(R.id.edtStock).text.toString().toFloat()

            GlobalScope.launch {
                val existingArticle = dao.getArticle(code)
                if (existingArticle == null) {
                    val article = Article(code, description, family, price, priceWithIVA, stock)
                    dao.insert(article)
                    val intent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Show error message indicating that the code already exists
                    runOnUiThread {
                        val snackbar = Snackbar.make(rootView, "Article with code $code already exists!", Snackbar.LENGTH_SHORT)
                        //set color red for snackbar hexadecimal code
                        snackbar.view.setBackgroundColor(0xFFD32F2F.toInt())
                        snackbar.show()
                    }
                }
            }
        }

        dialog.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        dialog.show()
    }

    fun editArticle(article: Article) {
        val view = layoutInflater.inflate(R.layout.articles_dialog, null)

        // Obtiene una referencia al Spinner
        val spinner: Spinner = view.findViewById(R.id.spnFamily)
        val options = arrayOf(" ", "software", "hardware", "Altres")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)
        spinner.adapter = adapter

        //obtener el codigo del articles
        val code = article.codeArticle

        val codeEditText = view.findViewById<EditText>(R.id.edtCode)
        codeEditText.isEnabled = false  // Desactivar la edición del código
        codeEditText.setText(code) // Establecer el código del artículo

        val dialog = AlertDialog.Builder(this)
        dialog.setView(view)
        dialog.setTitle("Edit Article")

        val rootView = window.decorView.findViewById<View>(android.R.id.content) // Obtener la vista principal de la actividad

        dialog.setPositiveButton("Edit") { dialog, which ->
            val code = codeEditText.text.toString()
            val description = view.findViewById<EditText>(R.id.edtDescription).text.toString()

            val spinner = view.findViewById<Spinner>(R.id.spnFamily)
            val family = spinner.selectedItem.toString()

            val price = view.findViewById<EditText>(R.id.edtPrice).text.toString().toFloat()
            // Calcula el precio con IVA
            val priceWithIVA = price * (1 + (IVA_PERCENTAGE / 100)).toFloat()

            val stock = view.findViewById<EditText>(R.id.edtStock).text.toString().toFloat()

            GlobalScope.launch {
                val existingArticle = dao.getArticle(code)
                if (existingArticle != null) {
                    val article = Article(code, description, family, price, priceWithIVA, stock)
                    dao.update(article)
                    val intent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Show error message indicating that the code already exists
                    runOnUiThread {
                        val snackbar = Snackbar.make(rootView, "Article with code $code already exists!", Snackbar.LENGTH_SHORT)
                        //set color red for snackbar hexadecimal code
                        snackbar.view.setBackgroundColor(0xFFD32F2F.toInt())
                        snackbar.show()
                    }
                }
            }
        }

        dialog.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        dialog.show()
    }


}