package com.example.newsapptest

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapter
    private lateinit var articlesList: ArrayList<Article>
    private lateinit var allDataTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        articlesList = ArrayList()
        adapter = NewsAdapter(articlesList) { url -> openArticle(url) }
        recyclerView.adapter = adapter

        allDataTextView = findViewById(R.id.allDataTextView)

        FetchNewsTask().execute("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json")
    }

    private fun openArticle(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    inner class FetchNewsTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg urls: String?): String {
            val urlString = urls[0]
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection

            try {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                return response.toString()
            } finally {
                connection.disconnect()
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()) {
                parseNewsData(result)
            }
        }
    }

    private fun parseNewsData(jsonString: String) {
        val jsonObject = JSONObject(jsonString)
        val articlesArray = jsonObject.getJSONArray("articles")

        val allData = StringBuilder()

        for (i in 0 until articlesArray.length()) {
            val articleObject = articlesArray.getJSONObject(i)
            val source = articleObject.getJSONObject("source")
            val sourceId = source.getString("id")
            val sourceName = source.getString("name")
            val author = articleObject.getString("author")
            val title = articleObject.getString("title")
            val description = articleObject.getString("description")
            val url = articleObject.getString("url")
            val urlToImage = articleObject.getString("urlToImage")
            val publishedAt = articleObject.getString("publishedAt")
            val content = articleObject.getString("content")

            val article = Article(
                sourceId,
                sourceName,
                author,
                title,
                description,
                url,
                urlToImage,
                publishedAt,
                content
            )
            articlesList.add(article)

            allData.append("Source ID: $sourceId\n")
            allData.append("Source Name: $sourceName\n")
            allData.append("Author: $author\n")
            allData.append("Title: $title\n")
            allData.append("Description: $description\n")
            allData.append("URL: $url\n")
            allData.append("URL to Image: $urlToImage\n")
            allData.append("Published At: $publishedAt\n")
            allData.append("Content: $content\n\n")
        }

        adapter.notifyDataSetChanged()
//        allDataTextView.text = allData.toString()
    }
}
